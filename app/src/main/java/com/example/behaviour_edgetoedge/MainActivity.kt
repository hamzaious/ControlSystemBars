package com.example.behaviour_edgetoedge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.behaviour_edgetoedge.systembars.controlSystemBars

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        controlSystemBars(
            type = SystemBarsAction.SHOW_SYSTEM_BARS,
            topInsetPadding = true,
            bottomInsetPadding = true
        )
    }

}