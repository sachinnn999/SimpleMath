package com.sachinnn.simplemath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var btnStartGame: Button
    private lateinit var btnAbout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartGame = findViewById(R.id.btn_start_game)
        btnAbout = findViewById(R.id.btn_about)

        btnStartGame.setOnClickListener{ createStartGameActivity() }
        btnAbout.setOnClickListener{ showAbout() }
        this.title = getString(R.string.home)
    }

    /**
     * calling GameActivity
     */
    private fun createStartGameActivity() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    /**
     * calling AboutActivity
     */
    private fun showAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
}