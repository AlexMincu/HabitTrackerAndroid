package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.FragmentHabitsNavBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class HabitsNavFragment : Fragment() {
    private lateinit var binding: FragmentHabitsNavBinding

    private var database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://habittracker-cdded-default-rtdb.europe-west1.firebasedatabase.app/")
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser = auth.currentUser!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    private lateinit var habitList: ArrayList<Habit>
    private lateinit var habitAdapter: HabitAdapter

    private lateinit var floatingActionButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHabitsNavBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        getHabitsData()

        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList(newText)
                return true
            }
        })

        floatingActionButton = binding.floatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(activity, AddHabit::class.java)
            startActivity(intent)
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                habitList.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }


    private fun filteredList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Habit>()

            for (habit in habitList) {
                if (habit.name!!.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(habit)
                }
            }

            if (filteredList.isNotEmpty()) {
                habitAdapter.setFilteredList(filteredList)
            } else {
                habitAdapter.setFilteredList(filteredList)
                Toast.makeText(activity, "No habits found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getHabitsData() {
        val userRef = database.getReference("users").child(user.uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                habitList = arrayListOf()

                if (snapshot.exists()) {
                    for (habitSnapshot in snapshot.children) {

                        val habit = habitSnapshot.getValue(Habit::class.java)
                        if (habit != null) {
                            habitList.add(habit)
                        }
                    }
                    habitAdapter = HabitAdapter(habitList)
                    recyclerView.adapter = habitAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}