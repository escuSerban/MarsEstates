package com.example.marsestates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * This main activity is just a container for our fragments.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}