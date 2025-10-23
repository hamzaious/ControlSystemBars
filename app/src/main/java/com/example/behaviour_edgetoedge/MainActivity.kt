package com.example.behaviour_edgetoedge

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.behaviour_edgetoedge.systembars.controlSystemBars

class MainActivity : AppCompatActivity() {
    
    private lateinit var statusText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize views
        statusText = findViewById(R.id.statusText)
        
        // Setup button click listeners
        setupButtonListeners()
        
        // Initial setup - show system bars with padding
        controlSystemBars(
            type = SystemBarsAction.SHOW_SYSTEM_BARS,
            topInsetPadding = true,
            bottomInsetPadding = true
        )
        updateStatus("Initial: Show System Bars")
    }
    
    private fun setupButtonListeners() {
        findViewById<Button>(R.id.btnShowSystemBars).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.SHOW_SYSTEM_BARS,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Show System Bars - Both status and navigation bars visible")
        }
        
        findViewById<Button>(R.id.btnHideStatusShowNav).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.HIDE_STATUS_SHOW_NAV,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Hide Status, Show Nav - Only navigation bar visible")
        }

        findViewById<Button>(R.id.btnShowSystemBarsWithoutPadding).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.SHOW_SYSTEM_BARS,
                topInsetPadding = false,
                bottomInsetPadding = false
            )
            updateStatus("Hide Status, Show Nav - Only navigation bar visible")
        }

        findViewById<Button>(R.id.btnHideSystemBarsWithoutPadding).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.HIDE_SYSTEM_BARS,
                topInsetPadding = false,
                bottomInsetPadding = false
            )
            updateStatus("Hide Status, Show Nav - Only navigation bar visible")
        }
        
        findViewById<Button>(R.id.btnHideNavShowStatus).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.HIDE_NAV_SHOW_STATUS,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Hide Nav, Show Status - Only status bar visible")
        }
        
        findViewById<Button>(R.id.btnHideSystemBars).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.HIDE_SYSTEM_BARS,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Hide System Bars - Immersive mode (swipe to reveal)")
        }
        
        findViewById<Button>(R.id.btnHideKeyboard).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.HIDE_KEYBOARD,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Hide Keyboard - System bars unchanged")
        }
        
        findViewById<Button>(R.id.btnShowKeyboard).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.SHOW_KEYBOARD,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Show Keyboard - System bars unchanged")
        }
        
        findViewById<Button>(R.id.btnHideAll).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.HIDE_ALL,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Hide All - Status bar, navigation bar, and keyboard hidden")
        }
        
        findViewById<Button>(R.id.btnShowAll).setOnClickListener {
            controlSystemBars(
                type = SystemBarsAction.SHOW_ALL,
                topInsetPadding = true,
                bottomInsetPadding = true
            )
            updateStatus("Show All - Status bar, navigation bar, and keyboard visible")
        }
    }
    
    private fun updateStatus(message: String) {
        statusText.text = "Status: $message"
    }
}