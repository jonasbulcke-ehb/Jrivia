package be.ehb.gdt.jrivia.activities

import android.content.Intent
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
        val numberOfQuestions = intent.getIntExtra(GameLoadingActivity.NUMBER_OF_QUESTIONS, -1)

        if (clues == null || numberOfQuestions == -1) {
            Snackbar.make(
                findViewById(R.id.gameLayout),
                getString(R.string.game_start_error),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.go_back) { finish() }
                .setActionTextColor(ContextCompat.getColor(this, R.color.secondaryLightColor))
                .show()
        } else {
            val game = Game(clues, numberOfQuestions)
            gameViewModel.game = game
            gameViewModel.game.start()
        }

        updateView()
    }

    private fun onNextClick() {
        gameViewModel.currentClue.guess = binding.answerEditText.text.trim().toString()
        if (gameViewModel.index < gameViewModel.game.numberOfQuestions - 1) {
            gameViewModel.moveNext()
            updateView()
        } else {
            gameViewModel.game.finish()
//            finish()
            Intent(this, GameOverviewActivity::class.java)
                .also { startActivity(it) }
        }
    }

    private fun onBackClick() {
        gameViewModel.currentClue.guess = binding.answerEditText.text.trim().toString()
        gameViewModel.moveBack()
        updateView()
    }

    private fun updateView() {
        binding.questionNumberTextView.text =
            getString(R.string.question_number, (gameViewModel.index + 1))
        binding.gameQuestionTextView.text = gameViewModel.currentClue.question
        binding.answerEditText.setText(gameViewModel.currentClue.guess ?: "")
        binding.questionsProgressIndicator.progress =
            ((gameViewModel.index.toDouble() / gameViewModel.game.numberOfQuestions) * 100).toInt()
        binding.nextButton.text =
            if (gameViewModel.index >= gameViewModel.game.numberOfQuestions - 1) getString(R.string.finish)
            else getString(R.string.next)
        binding.backButton.isEnabled = gameViewModel.index > 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("BACK", "onBackPressed invoked")
    }
}