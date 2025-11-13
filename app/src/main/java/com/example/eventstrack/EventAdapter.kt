package com.example.eventstrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventstrack.api.Event
import android.widget.Button

class EventAdapter(private var events: List<Event>,
                   private val context: Context,
                   private val onToggleSave: (Event, Boolean) -> Unit
) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val btnSave: Button = itemView.findViewById(R.id.btnSave)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvName.text = event.title
        holder.tvDate.text = "📅 ${event.start_utc?:"Date TBA"}"
        holder.tvLocation.text = "📍 ${event.venue_name?:"Location TBA}"}"
        holder.tvDescription.text = event.description

        // Check if this event is saved
        val prefs = context.getSharedPreferences("saved_events", Context.MODE_PRIVATE)
        val savedIds = prefs.getStringSet("event_ids", emptySet()) ?: emptySet()
        val isSaved = savedIds.contains(event.id.toString())

        // Update button text based on saved status
        holder.btnSave.text = if (isSaved) "Unsave" else "Save"

        holder.btnSave.setOnClickListener {
            onToggleSave(event, isSaved)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
