package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.databinding.ActivityAddHabitBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AddHabit : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitBinding

    private var database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://habittracker-cdded-default-rtdb.europe-west1.firebasedatabase.app/")
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser = auth.currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addHabitButton.setOnClickListener {
            val userRef = database.getReference("users").child(user.uid)

            val habitName = binding.habitNameInput.text.toString()
            userRef.push().setValue(Habit(habitName))

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}