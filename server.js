// server.js
require('dotenv').config();
const express = require('express');
const cors = require('cors');
const Database = require('better-sqlite3');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');


const db = new Database('./events.db');
const app = express();
app.use(cors());
app.use(express.json());

const JWT_SECRET = process.env.JWT_SECRET || 'devsecret';

// Initialize DB (run once)
function initDB() {
  db.prepare(`
    CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      email TEXT UNIQUE,
      password_hash TEXT,
      display_name TEXT
    );
  `).run();

  db.prepare(`
    CREATE TABLE IF NOT EXISTS events (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      title TEXT,
      description TEXT,
      category TEXT,
      start_utc TEXT,
      end_utc TEXT,
      venue_name TEXT,
      lat REAL,
      lng REAL,
      image_url TEXT
    );
  `).run();

  db.prepare(`
    CREATE TABLE IF NOT EXISTS saved_events (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      user_id INTEGER,
      event_id INTEGER,
      saved_at INTEGER,
      FOREIGN KEY(user_id) REFERENCES users(id),
      FOREIGN KEY(event_id) REFERENCES events(id)
    );
  `).run();

  // Insert sample events if table empty
  const count = db.prepare('SELECT COUNT(*) as c FROM events').get().c;
  if (count === 0) {
    const insert = db.prepare(`INSERT INTO events (title, description, category, start_utc, end_utc, venue_name, lat, lng, image_url)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`);

    insert.run("Cape Town Night Market", "Local food and crafts", "markets", "2025-10-15T17:00:00Z", "2025-10-15T22:00:00Z", "V&A Waterfront", -33.905, 18.422, "");
    insert.run("Johannesburg Jazz Evening", "An evening of jazz", "music", "2025-10-20T19:00:00Z", "2025-10-20T22:00:00Z", "Mzansi Jazz Hall", -26.204, 28.047, "");
  }
}

initDB();

/* AUTH ROUTES */

// Register
app.post('/auth/register', async (req, res) => {
  const { email, password, displayName } = req.body;
  if (!email || !password) return res.status(400).json({ error: 'Email and password required' });

  const saltRounds = 10;
  try {
    const hash = await bcrypt.hash(password, saltRounds);
    const stmt = db.prepare('INSERT INTO users (email, password_hash, display_name) VALUES (?, ?, ?)');
    const info = stmt.run(email, hash, displayName || null);
    const user = { id: info.lastInsertRowid, email, displayName };
    // create token
    const token = jwt.sign({ userId: user.id, email }, JWT_SECRET, { expiresIn: '7d' });
    res.json({ user, token });
  } catch (err) {
    if (err.code === 'SQLITE_CONSTRAINT_UNIQUE') {
      res.status(400).json({ error: 'Email already exists' });
    } else {
      console.error(err);
      res.status(500).json({ error: 'Server error' });
    }
  }
});

// Login
app.post('/auth/login', async (req, res) => {
  const { email, password } = req.body;
  const row = db.prepare('SELECT id, email, password_hash, display_name FROM users WHERE email = ?').get(email);
  if (!row) return res.status(401).json({ error: 'Invalid credentials' });

  const match = await bcrypt.compare(password, row.password_hash);
  if (!match) return res.status(401).json({ error: 'Invalid credentials' });

  const token = jwt.sign({ userId: row.id, email: row.email }, JWT_SECRET, { expiresIn: '7d' });
  res.json({ user: { id: row.id, email: row.email, displayName: row.display_name }, token });
});

/* EVENTS - public read endpoints */

// Simple event listing (supports ?q= keyword and ?category=)
app.get('/events', (req, res) => {
  const { q = '', category } = req.query;
  let stmt;
  if (category) {
    stmt = db.prepare(`SELECT * FROM events WHERE category = ? AND (title LIKE ? OR description LIKE ?) ORDER BY start_utc`);
    res.json({ events: stmt.all(category, `%${q}%`, `%${q}%`) });
  } else {
    stmt = db.prepare(`SELECT * FROM events WHERE (title LIKE ? OR description LIKE ?) ORDER BY start_utc`);
    res.json({ events: stmt.all(`%${q}%`, `%${q}%`) });
  }
});

// Event by id
app.get('/events/:id', (req, res) => {
  const id = req.params.id;
  const row = db.prepare('SELECT * FROM events WHERE id = ?').get(id);
  if (!row) return res.status(404).json({ error: 'Not found' });
  res.json({ event: row });
});

/* SYNC saved events (requires Authorization header with Bearer token) */
function getUserFromToken(req) {
  const auth = req.headers.authorization;
  if (!auth) return null;
  const parts = auth.split(' ');
  if (parts.length !== 2) return null;
  const token = parts[1];
  try {
    const payload = jwt.verify(token, JWT_SECRET);
    return payload; // contains userId, email
  } catch(e) {
    return null;
  }
}

app.post('/sync/saved', (req, res) => {
  const user = getUserFromToken(req);
  if (!user) return res.status(401).json({ error: 'Unauthorized' });
  const saved = req.body.saved; // expect [{ eventId, savedAt }]
  if (!Array.isArray(saved)) return res.status(400).json({ error: 'Bad payload' });

  const insert = db.prepare('INSERT INTO saved_events (user_id, event_id, saved_at) VALUES (?, ?, ?)');
  const deleted = db.prepare('DELETE FROM saved_events WHERE user_id = ? AND event_id = ?');
  // naive approach: remove duplicates and insert
  saved.forEach(s => {
    const exists = db.prepare('SELECT id FROM saved_events WHERE user_id = ? AND event_id = ?').get(user.userId, s.eventId);
    if (!exists) {
      insert.run(user.userId, s.eventId, s.savedAt || Date.now());
    }
  });
  res.json({ status: 'ok' });
});

const port = process.env.PORT || 4000;

app.listen(port, '0.0.0.0', () => {
  console.log(`✅ Server running on http://0.0.0.0:${port}`);
});

app.post('/events', (req, res) => {
  const { title, description, category, start_utc, end_utc, venue_name, lat, lng, image_url } = req.body;
  if (!title || !description) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  const stmt = db.prepare(`INSERT INTO events (title, description, category, start_utc, end_utc, venue_name, lat, lng, image_url)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`);
  const info = stmt.run(title, description, category || '', start_utc || '', end_utc || '', venue_name || '', lat || 0, lng || 0, image_url || '');
  
  res.status(201).json({ id: info.lastInsertRowid });
});


/*
let events = [
  { id: 1, name: 'Tech Meetup', date: '2025-10-15', location: 'Cape Town', description: 'Discussing local tech trends.' }
];

// Existing route
app.get('/events', (req, res) => {
  res.json(events);
});

// 🆕 POST route to create new events
app.post('/events', (req, res) => {
  const { name, date, location, description } = req.body;

  if (!name || !date || !location || !description) {
    return res.status(400).json({ error: 'All fields are required' });
  }

  const newEvent = {
    id: events.length + 1,
    name,
    date,
    location,
    description
  };

  events.push(newEvent);
  res.status(201).json(newEvent);
});
*/

