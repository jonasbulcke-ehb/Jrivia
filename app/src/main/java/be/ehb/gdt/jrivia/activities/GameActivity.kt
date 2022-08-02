package be.ehb.gdt.jrivia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.ehb.gdt.jrivia.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}