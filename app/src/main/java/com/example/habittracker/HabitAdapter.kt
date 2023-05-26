package com.example.habittracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(private var habitList: List<Habit>) :
    RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(view)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val currentHabit = habitList[position]
        holder.name.text = currentHabit.name
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.habitTitle)
    }

    fun setFilteredList(habitList: List<Habit>) {
        this.habitList = habitList
        notifyDataSetChanged()
    }

}