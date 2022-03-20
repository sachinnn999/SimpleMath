package com.sachinnn.simplemath

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AboutActivity : AppCompatActivity() {
    private lateinit var imgProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        this.title = getString(R.string.about)
        imgProfile  = findViewById(R.id.profile_pic)
        imgProfile.setOnClickListener{callIntent()}
    }

    private fun callIntent() {
        val implicit = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_url)))
        startActivity(implicit)
    }
}