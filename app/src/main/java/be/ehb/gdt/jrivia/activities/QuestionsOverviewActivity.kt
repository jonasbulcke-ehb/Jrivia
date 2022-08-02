package be.ehb.gdt.jrivia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.ehb.gdt.jrivia.databinding.ActivityQuestionsOverviewBinding

class QuestionsOverviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionsOverviewBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsOverviewBinding.inflate(layoutInflater)
        val view = binding.root
        
        setContentView(view)
    }
}