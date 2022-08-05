package be.ehb.gdt.jrivia.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityMainBinding
import be.ehb.gdt.jrivia.room.DailyClueRepository
import be.ehb.gdt.jrivia.services.DailyClueFetchService
import java.util.*


class MainActivity : AppCompatActivity() {
    private var timeOnPressed: Long = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.singlePlayerButton.setOnClickListener {
            Intent(this, GameLoadingActivity::class.java)
                .also { startActivity(it) }
        }

        binding.homeScoreboardButton.setOnClickListener {
            Intent(this, ScoreBoardActivity::class.java)
                .also { startActivity(it) }
        }

        setContentView(binding.root)

        val alarmManager: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        val intent = Intent(applicationContext, DailyClueFetchService::class.java)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            .also {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    24 * 60 * 60 * 1000,
                    it
                )
            }


    }

    /** source: https://www.geeksforgeeks.org/how-to-implement-press-back-again-to-exit-in-android/ */
    override fun onBackPressed() {
        if (timeOnPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT)
                .show()
        }
        timeOnPressed = System.currentTimeMillis()
    }
}