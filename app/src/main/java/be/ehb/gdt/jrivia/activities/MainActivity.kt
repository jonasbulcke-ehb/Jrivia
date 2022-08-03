package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.ehb.gdt.jrivia.R


class MainActivity : AppCompatActivity() {
    private var timeOnPressed: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** source: https://www.geeksforgeeks.org/how-to-implement-press-back-again-to-exit-in-android/ */
    override fun onBackPressed() {
        if (timeOnPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()
        }
        timeOnPressed = System.currentTimeMillis()
    }
}