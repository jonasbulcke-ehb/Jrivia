package be.ehb.gdt.jrivia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityDailyQuestsBinding
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModel
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModelFactory

class DailyQuestsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailyQuestsBinding
    private val dailyQuestViewModel: DailyQuestViewModel by viewModels {
        DailyQuestViewModelFactory((application as JriviaApplication).dailyQuestRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyQuestsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        supportActionBar?.title = getString(R.string.daily_quests)
    }
}