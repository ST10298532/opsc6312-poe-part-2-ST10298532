# 📱 EventsTrack – OPSC6312 POE Part 2

**Student:** ST10298532  
**Module:** OPSC6312 – Open Source Coding (Intermediate)
**Project:** EventsTrack (Android + Node.js API)  
**Part:** 2 – Prototype Implementation & GitHub Version Control  

---

## 🧾 Project Overview
EventsTrack is an Android mobile application that allows users to:
- Register and log in securely.
- Browse and view upcoming events (fetched from a REST API).
- Create and save their own events.
- Save/unsave events locally to a “Saved Events” list.
- View and manage saved events.
- Access app settings and log out securely.

The app communicates with a **Node.js + Express API** backend connected to a **SQLite** database for managing users and events.

---

## 🧠 System Architecture
**Frontend (Android):**
- Developed in **Kotlin** using **Android Studio**.
- Uses **Retrofit** for API communication.
- Follows an MVVM-style modular structure.
- Local data is stored using **SharedPreferences**.

**Backend (API Server):**
- Built with **Node.js** and **Express.js**.
- Database: **SQLite** (via better-sqlite3).
- Handles:
  - User registration and authentication.
  - CRUD operations for events.
  - Synchronization of saved events.
- Runs on `http://0.0.0.0:4000` (can be accessed via local IP).

---

## ⚙️ Features Implemented
| Feature | Description |
|----------|--------------|
| **User Authentication** | Users can register and log in securely (hashed passwords, JWT token). |
| **Event Creation** | Users can create and submit custom events via API. |
| **Event Listing** | Displays all events from API dynamically using RecyclerView. |
| **Save/Unsave Events** | Users can save or remove events from their Saved list. |
| **Saved Events Page** | Displays only locally saved events, with option to remove. |
| **Settings Screen** | Allows user to configure app-related preferences. |
| **Logout Functionality** | Clears user session and redirects to login screen. |
| **API Integration** | REST API integrated using Retrofit and tested with Node server. |

---

## 🧩 REST API Endpoints
**Base URL:**  


| Method | Endpoint | Description |
|--------|-----------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login existing user |
| GET | `/events` | Fetch all events |
| POST | `/events` | Create new event |
| POST | `/sync/saved` | Sync saved events for logged-in user |

---

## 🧰 Technologies Used
**Mobile App:**
- Kotlin  
- Android Studio  
- Retrofit  
- SharedPreferences  
- RecyclerView  

**Backend:**
- Node.js  
- Express.js  
- better-sqlite3  
- bcrypt  
- JWT (jsonwebtoken)  
- dotenv  
- cors  

---

## 🧑‍💻 Installation & Setup

### 1️⃣ Backend (API)
```bash
cd eventstrack-api
npm install
node server.js

The API will start at:

http://0.0.0.0:4000

2️⃣ Android App

Open project in Android Studio.

Update ApiClient.kt → BASE_URL to match your local network IP, e.g.:

private const val BASE_URL = "http://192.168.88.172:4000/"


Connect phone & ensure both devices share the same Wi-Fi network.

Enable USB debugging → Run the app on a physical Android device.

📦 Version Control & CI

Version control managed via GitHub.

Repository initialized with .gitignore and README.

Multiple commits documenting development progress.

Automated testing/build can be configured with GitHub Actions.

Repository:
👉 https://github.com/ST10298532/opsc6312-poe-part-2-ST10298532

📸 Screens Implemented

Login / Register

Home Screen

Events List

Add Event

Saved Events

Settings

🚀 Next Steps (Part 3)

Implement improved UI and animations.

Enable API synchronization for saved events.

Add user profile management.

Deploy the Node.js API online (e.g., Render/Heroku).

Finalize APK and create demonstration video.

🏁 Summary

EventsTrack demonstrates a fully functional Android mobile app integrated with a RESTful Node.js API backend.
It supports authentication, event management, and local storage, meeting all requirements up to Part 2 of the POE.
