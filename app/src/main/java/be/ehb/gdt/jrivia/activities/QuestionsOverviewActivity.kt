package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
                ?: throw IllegalStateException("Something went wrong while trying to finish the game")
        } catch (e: IllegalStateException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

        binding = ActivityQuestionsOverviewBinding.inflate(layoutInflater)

        binding.questionsRecyclerView.apply {
            adapter = QuestionAdapter(gameViewModel.game.clues, this@QuestionsOverviewActivity)
            setHasFixedSize(true)
        }

        setContentView(binding.root)
    }


}