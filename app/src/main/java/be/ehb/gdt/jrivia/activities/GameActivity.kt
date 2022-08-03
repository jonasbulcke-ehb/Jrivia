package be.ehb.gdt.jrivia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityGameBinding
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.models.Game
import be.ehb.gdt.jrivia.models.viewmodels.GameViewModel
import com.google.android.material.snackbar.Snackbar

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.answerEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    onNextClick()
                    true
                }
                else -> false
            }
        }
        binding.nextButton.setOnClickListener { onNextClick() }
        binding.backButton.setOnClickListener { onBackClick() }


        val clues =
            intent.getParcelableArrayExtra(GameLoadingActivity.EXTRA_CLUES)?.map { it as Clue }

        if (clues == null) {
            Snackbar.make(
                findViewById(R.id.gameLayout),
                getString(R.string.game_start_error),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.go_back) { finish() }
                .setActionTextColor(ContextCompat.getColor(this, R.color.secondaryLightColor))
                .show()
        } else {
            val game = Game(clues)
            gameViewModel.game = game
        }

        updateView()
    }

    private fun onNextClick() {
        if (gameViewModel.index < Game.NUMBER_OF_QUESTIONS) {
            gameViewModel.currentClue.guess = binding.answerEditText.text.trim().toString()
            gameViewModel.moveNext()
            updateView()
        } else {
            TODO()
        }
    }

    private fun onBackClick() {
        gameViewModel.moveBack()
        updateView()
    }

    private fun updateView() {
        binding.questionNumberTextView.text =
            getString(R.string.question_number, (gameViewModel.index + 1))
        binding.gameQuestionTextView.text = gameViewModel.currentClue.question
        binding.answerEditText.setText(gameViewModel.currentClue.guess ?: "")
        binding.questionsProgressIndicator.progress =
            ((gameViewModel.index.toDouble() / Game.NUMBER_OF_QUESTIONS) * 100).toInt()
        binding.nextButton.text =
            if (gameViewModel.index >= Game.NUMBER_OF_QUESTIONS - 1) getString(R.string.finish)
            else getString(R.string.next)
        binding.backButton.isEnabled = gameViewModel.index > 0
    }
}