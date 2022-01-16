package com.example.imbd.DAO

import android.database.Cursor
import androidx.room.*
import com.example.imbd.entity.Film

@Dao
interface FilmDao {
    @Query("select * from Film where _id = :filmId")
    fun getById(filmId: Int): Film

    @Query(" SELECT * FROM Film WHERE film_watched = 1 ")
    fun getCursor(): Cursor?

    @Query("SELECT * FROM Film WHERE film_watched = 0")
    fun getCursorToWatch(): Cursor?

    @Query("SELECT SUM(film_duration) from Film where film_watched = 1")
    fun sumFilmsDuration(): Int?

    @Query ("UPDATE Film SET film_watched = 1 WHERE _id = :id")
    fun updateWatchedById(id: Long)

    @Update
    fun updateFilm(film: Film)

    @Insert
    fun insertAll(vararg film: Film)

    @Query("DELETE FROM Film WHERE _id = :filmId")
    fun deleteById(filmId : Long)

}