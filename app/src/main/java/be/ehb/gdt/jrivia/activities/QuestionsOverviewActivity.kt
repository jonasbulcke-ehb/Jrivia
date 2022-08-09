package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.adapters.QuestionAdapter
import be.ehb.gdt.jrivia.databinding.ActivityQuestionsOverviewBinding
import be.ehb.gdt.jrivia.viewmodels.GameViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames

class QuestionsOverviewActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityQuestionsOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            gameViewModel.game = intent.getParcelableExtra(IntentExtraNames.GAME)
                ?: throw IllegalStateException(getString(R.string.game_overview_error))
        } catch (e: IllegalStateException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            finish()
        }

        binding = ActivityQuestionsOverviewBinding.inflate(layoutInflater)

        binding.questionsRecyclerView.apply {
            adapter = QuestionAdapter(gameViewModel.game.clues, this@QuestionsOverviewActivity)
            setHasFixedSize(true) // improves the performance by letting the system know the list won't grow anymore
        }

        supportActionBar?.title = getString(R.string.questions_overview)
        setContentView(binding.root)
    }


}