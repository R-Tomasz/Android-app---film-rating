package com.example.imbd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.imbd.language.LocaleHelper

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val filmsBtn: Button = findViewById(R.id.myFilmsBtn)
        val toWatchBtn: Button = findViewById(R.id.toWatchBtn)
        val statBtn: Button = findViewById(R.id.statisticsBtn)
        val aboutBtn: Button = findViewById(R.id.aboutBtn)

        val langPlBtn: ImageButton = findViewById(R.id.langPlBtn)
        val langEnBtn: ImageButton = findViewById(R.id.langEnBtn)
        val langDeBtn: ImageButton = findViewById(R.id.langDeBtn)

        filmsBtn.setOnClickListener {
            val intent = Intent(this, WatchedFilms::class.java)
            intent.putExtra("watched", true)
            startActivity(intent)
        }

        toWatchBtn.setOnClickListener {
            val intent = Intent(this, WatchedFilms::class.java)
            intent.putExtra("watched", false)
            startActivity(intent)
        }

        statBtn.setOnClickListener {
            val intent = Intent(this, Statistics::class.java)
            startActivity(intent)
        }

        aboutBtn.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }


        langPlBtn.setOnClickListener {
            LocaleHelper.setLocale(this, "PL")
            refresh()

        }
        langEnBtn.setOnClickListener {
            LocaleHelper.setLocale(this, "en")
            refresh()
        }
        langDeBtn.setOnClickListener {
            LocaleHelper.setLocale(this, "de")
            refresh()
        }
    }


    private fun refresh() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

}