package com.example.alarmy

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlarmAdapter(
    private val context: Context,
    private val alarms: MutableList<Alarm>,
    private val onItemClick: (Int) -> Unit,
    private val onSoundClick: (Int) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.time_text)
        val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        val alarmSwitch: Switch = itemView.findViewById(R.id.alarm_switch)
        val soundTextView: TextView = itemView.findViewById(R.id.sound_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.timeTextView.text = alarm.time
        holder.dateTextView.text = alarm.date
        holder.alarmSwitch.isChecked = alarm.isActive
        holder.soundTextView.text = alarm.soundUri ?: "No sound selected"

        holder.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d("AlarmAdapter", "Switch clicked for alarm at position $position, isChecked: $isChecked")
            alarm.isActive = isChecked
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        holder.soundTextView.setOnClickListener {
            onSoundClick(position)
        }
    }

    override fun getItemCount(): Int {
        return alarms.size
    }
}
