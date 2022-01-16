package com.example.imbd


import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.imbd.database.AppDatabase
import com.example.imbd.entity.Film


class SingleFilm : AppCompatActivity() {

    private var tempForId: Long = 0
    private val token: Int = 1
    private lateinit var film: Film
    private lateinit var title: TextView
    private lateinit var type: TextView
    private lateinit var year: TextView
    private lateinit var director: TextView
    private lateinit var notes: TextView
    private lateinit var rating: RatingBar
    private lateinit var duration: TextView
    private lateinit var database: AppDatabase
    private lateinit var calendar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_film)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "films")
            .allowMainThreadQueries().build()

        //id from WatchedFilms, used to make query and get object with this id
        val id: Long = intent.getLongExtra("filmId", 0)

        // will need this id outside onCreate fun
        tempForId = id

        film = database.filmDao().getById(id.toInt()) // get film with specified, received id

        title = findViewById(R.id.singleFilmTitleTextView)
        type = findViewById(R.id.singleFilmTypeTextView)
        year = findViewById(R.id.singleFilmYearTextView)
        director = findViewById(R.id.singleFilmDirectorTextView)
        notes = findViewById(R.id.singleFilmNotesTextView)
        rating = findViewById(R.id.singleFilmRatingBar)
        duration = findViewById(R.id.singleFilmDurationTextView)
        calendar = findViewById(R.id.singleFilmCalendarIcon)


        //set fields on single film layout
        title.text = film.title
        if (film.type != null) type.text = film.type
        if (film.release_date != null) {
            calendar.visibility = View.VISIBLE
            year.text = film.release_date.toString()
        } else {
            calendar.visibility = View.INVISIBLE
        }
        if (film.director != null) director.text = film.director
        if (film.notes != null) notes.text = film.notes
        if (film.duration != null) {
            duration.text = film.duration.toString()
            duration.append(" min")
        }
        rating.rating = film.rating
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //set custom menu from R.menu/
        menuInflater.inflate(R.menu.menu_items, menu)

        //get option item from menu, will need to check if hide it
        val markAsWatchedItem: MenuItem? = menu?.findItem(R.id.mark_as_watched)

        if (film.watched == true) {
            //hide option in menu if film is already watched
            markAsWatchedItem!!.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_single_film -> {
                val intent = Intent(this, AddFilm::class.java)
                intent.putExtra("filmId", getId())
                startActivityForResult(intent, token)
                true
            }
            R.id.delete_item -> {
                database.filmDao().deleteById(getId())
                finish()
                true
            }
            R.id.mark_as_watched -> {
                database.filmDao().updateWatchedById(getId())
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == token) {
            if (resultCode == RESULT_OK) {
                //get data from AddFilm form
                val titleReceived: String? = data?.getStringExtra("titleReceived")
                val typeReceived: String? = data?.getStringExtra("typeReceived")
                val yearReceived: Int? = data?.getIntExtra("yearReceived", 0)
                val directorReceived: String? = data?.getStringExtra("directorReceived")
                val notesReceived: String? = data?.getStringExtra("notesReceived")
                val ratingReceived: Float? = data?.getFloatExtra("ratingReceived", 0f)
                val durationReceived: Int? = data?.getIntExtra("durationReceived", 0)


                title.text = titleReceived
                type.text = typeReceived
                if (yearReceived == 0) {
                    year.text = ""
                    calendar.visibility = View.INVISIBLE
                } else {
                    calendar.visibility = View.VISIBLE
                    year.text = yearReceived.toString()
                }
                director.text = directorReceived
                notes.text = notesReceived
                rating.rating = ratingReceived!!
                duration.text = durationReceived.toString()
                duration.append(" min")
            }
        }
    }

    private fun getId(): Long {
        return tempForId
    }

}