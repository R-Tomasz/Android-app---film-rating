package com.example.imbd

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter


class FilmListAdapter(context: Context?, cursor: Cursor?) :
    CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.activity_film_item, parent, false)
    }

    override fun bindView(view: View, context: Context?, cursor: Cursor) {

        val filmTitle = view.findViewById<View>(R.id.filmTitleTextView) as TextView
        val filmDuration = view.findViewById<View>(R.id.filmDurationTextView) as TextView
        val filmRating = view.findViewById<View>(R.id.filmRatingTextView) as TextView

        val title: String = cursor.getString(cursor.getColumnIndexOrThrow("film_title"))
        val duration: Int = cursor.getInt(cursor.getColumnIndexOrThrow("film_duration"))
        val rating: Float = cursor.getFloat(cursor.getColumnIndexOrThrow("film_rating"))

        filmTitle.text = title
        filmDuration.text = duration.toString()
        filmDuration.append(" min")
        filmRating.text = rating.toString()
        filmRating.append("/5")
    }
}