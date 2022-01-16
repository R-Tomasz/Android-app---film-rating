package com.example.imbd.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Film (
    @ColumnInfo(name = "film_title")
    var title: String,
    @ColumnInfo(name = "film_type")
    var type: String?,
    @ColumnInfo(name = "film_rating")
    var rating: Float,
    @ColumnInfo(name = "film_director")
    var director: String?,
    @ColumnInfo(name = "film_notes")
    var notes: String?,
    @ColumnInfo(name = "film_release_date")
    var release_date: Int?,
    @ColumnInfo(name = "film_watched")
    var watched: Boolean?,
    @ColumnInfo(name = "film_duration")
    var duration: Int?
){
    @PrimaryKey(autoGenerate = true)
    var _id :Int = 0
}