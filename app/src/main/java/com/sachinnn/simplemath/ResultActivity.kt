package com.sachinnn.simplemath

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ResultActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private var correct: Int = 0
    private var total: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //setting activity title
        this.title = getString(R.string.result)

        //getting sharedpref and reading required keys
        prefs = getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE)
        total = prefs.getInt("total", total)
        correct = prefs.getInt("correct", correct)

        //setting up the score
        val viewScore:TextView = findViewById(R.id.lbl_score)
        viewScore.text = getString(R.string.txt_summary_score, correct, total)
    }
}