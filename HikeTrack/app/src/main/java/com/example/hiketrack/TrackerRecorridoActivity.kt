package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityTrackerRecorridoBinding

class TrackerRecorridoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackerRecorridoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackerRecorridoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
