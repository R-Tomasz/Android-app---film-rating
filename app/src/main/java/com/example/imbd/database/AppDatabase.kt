package com.example.imbd.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imbd.DAO.FilmDao
import com.example.imbd.entity.Film

@Database(entities = [Film::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun filmDao(): FilmDao
}