package be.ehb.gdt.jrivia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.ehb.gdt.jrivia.databinding.ActivityGameLoadingBinding

class GameLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameLoadingBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}