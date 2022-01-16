package com.example.imbd

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.imbd.database.AppDatabase
import com.example.imbd.entity.Film


class AddFilm : AppCompatActivity() {

    private lateinit var filmReceived: Film

    private lateinit var title: EditText
    private lateinit var type: EditText
    private lateinit var director: EditText
    private lateinit var releaseDate: EditText
    private lateinit var notes: EditText
    private lateinit var rating: RatingBar
    private lateinit var minutes: EditText
    private lateinit var ratingHeader: TextView
    private lateinit var database: AppDatabase
    private lateinit var headerTextView : TextView
    private var watched = false


    override fun onCreate(savedInstanceState: Bundle?) {
        database = Room.databaseBuilder(this, AppDatabase::class.java, "films")
            .allowMainThreadQueries().build()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_film)

        val intent = intent

        //get extra from WatchedFilms
        watched = intent.getBooleanExtra("watched", false)

        title = findViewById(R.id.titleEditText)
        type = findViewById(R.id.typeEditText)
        director = findViewById(R.id.directorEditText)
        releaseDate = findViewById(R.id.releaseDateEditText)
        notes = findViewById(R.id.notesEditText)
        rating = findViewById(R.id.ratingBar)
        minutes = findViewById(R.id.durationEditText)
        ratingHeader = findViewById(R.id.ratingBarTextView)
        headerTextView = findViewById(R.id.addOrEditHeaderTextView)

        headerTextView.text = getText(R.string.addFilmHeader)

        val submit: Button = findViewById(R.id.saveFormBtn)

        //check if activity started on result
        if (callingActivity != null) {
            val idReceived: Long = intent.getLongExtra("filmId", 0)

            //get and save object on which user clicked, will need to update it
            filmReceived = database.filmDao().getById(idReceived.toInt())
            setFields()
        }

        //reset fields, used after inserting object to database
        fun resetFields() {
            title.setText("")
            type.setText("")
            director.setText("")
            releaseDate.setText("")
            notes.setText("")
            rating.rating = 0.0f
            minutes.setText("")
        }

        fun saveToDatabase() {
            val year: Int? = if (releaseDate.text?.toString() == "") {
                null
            } else {
                releaseDate.text.toString().toInt()
            }
            val duration: Int? = if (minutes.text?.toString() == "") {
                null
            } else {
                minutes.text.toString().toInt()
            }
            val film = Film(
                title.text.toString(),
                type.text?.toString(),
                rating.rating,
                director.text?.toString(),
                notes.text?.toString(),
                year,
                watched,
                duration
            )

            database.filmDao().insertAll(film)
            Toast.makeText(this, getString(R.string.successfullySavedObject), Toast.LENGTH_SHORT)
                .show()
        }

        submit.setOnClickListener {
            if (validateForm()) {
                //check if activity was started for result
                if (callingActivity == null) {
                    saveToDatabase()
                    resetFields()
                } else {
                    updateFilm()
                    sendDataBack()
                    finish()
                }
            }
        }
    }

    //set fields if activity called for result, it will help to edit fields
    private fun setFields() {
        headerTextView.text = getText(R.string.editFilmHeader)
        title.setText(filmReceived.title)
        rating.rating = filmReceived.rating
        if (filmReceived.type != null) type.setText(filmReceived.type)
        if (filmReceived.release_date != null) releaseDate.setText(filmReceived.release_date.toString())
        if (filmReceived.director != null) director.setText(filmReceived.director)
        if (filmReceived.notes != null) notes.setText(filmReceived.notes)
        if (filmReceived.duration != null) minutes.setText(filmReceived.duration.toString())
    }

    private fun validateForm(): Boolean {
        if (title.text.toString() == "") {
            title.error = getText(R.string.fieldRequired)
            return false
        }
        if (minutes.text.toString() == "") {
            minutes.error = getText(R.string.fieldRequired)
            return false
        }
        if (rating.rating == 0.0f) {
            ratingHeader.setTextColor(Color.RED)

            ratingHeader.text = getText(R.string.formFillRating)
            return false
        } else {
            ratingHeader.setTextColor(Color.BLACK)
            ratingHeader.text = getText(R.string.form_film_rating)
        }
        return true
    }

    //update query if activity called for result
    private fun updateFilm() {
        filmReceived.title = title.text.toString()
        filmReceived.type = type.text.toString()
        filmReceived.director = director.text.toString()
        filmReceived.duration = minutes.text.toString().toInt()
        filmReceived.release_date = if(releaseDate.text.toString() == ""){
            null
        } else{
            releaseDate.text.toString().toInt()
        }
        filmReceived.notes = notes.text.toString()
        filmReceived.rating = rating.rating
        database.filmDao().updateFilm(filmReceived)
    }

    //send data to calling (SingleFilm) activity
    private fun sendDataBack() {
        val returnIntent = Intent()
        returnIntent.putExtra("titleReceived", filmReceived.title)
        returnIntent.putExtra("typeReceived", filmReceived.type)
        returnIntent.putExtra("directorReceived", filmReceived.director)
        returnIntent.putExtra("durationReceived", filmReceived.duration)
        returnIntent.putExtra("yearReceived", filmReceived.release_date)
        returnIntent.putExtra("notesReceived", filmReceived.notes)
        returnIntent.putExtra("ratingReceived", filmReceived.rating)
        setResult(RESULT_OK, returnIntent)
    }
}