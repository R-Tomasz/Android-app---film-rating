package com.example.imbd

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.imbd.database.AppDatabase

class Statistics : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var timeSpent: TextView
    private lateinit var hoursSpent: TextView
    private lateinit var summary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "films")
            .allowMainThreadQueries().build()


        var time: Int? = database.filmDao().sumFilmsDuration()

        if (time == null) time = 0
        timeSpent = findViewById(R.id.minutesSpentTextView)
        hoursSpent = findViewById(R.id.hoursSpentTextView)
        summary = findViewById(R.id.statisticsSummaryTextView)

        timeSpent.text = time.toString()
        timeSpent.append(" min")

        hoursSpent.text = String.format("%.2f", time.toFloat() / 60)
        hoursSpent.append(" h")

        if (time != 0) {
            summary.text = getText(R.string.statistics_summary)
        }
    }
}