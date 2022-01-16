package com.example.imbd

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.imbd.database.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WatchedFilms : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var listView: ListView
    private var watched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watched_films)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "films")
            .allowMainThreadQueries().build()
        listView = findViewById(R.id.filmList)

        val intentToCheckSender: Intent = intent
        watched = intentToCheckSender.getBooleanExtra("watched", false)

        val addFilmFAB: FloatingActionButton = findViewById(R.id.addFilmFAB)

        listView.setOnItemClickListener { _: AdapterView<*>, _: View, _: Int, id: Long ->
            val intent = Intent(this, SingleFilm::class.java)
            intent.putExtra("filmId", id)
            startActivity(intent)
        }

        addFilmFAB.setOnClickListener {
            val intent = Intent(this, AddFilm::class.java)
            intent.putExtra(
                "watched",
                watched
            ) // activity AddFilm has to add film with proper "watched" tag
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //check which query need to be loaded, based on Home activity button clicked
        val cursor: Cursor? = if (watched) {
            //clicked Watched films button, display watched films
            database.filmDao().getCursor()
        } else {
            //clicked To watch button, display films to watch
            database.filmDao().getCursorToWatch()
        }
        val adapter = FilmListAdapter(this, cursor)
        listView.adapter = adapter
    }
}