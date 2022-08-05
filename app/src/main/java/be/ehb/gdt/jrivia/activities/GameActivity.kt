package be.ehb.gdt.jrivia.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityGameBinding
import be.ehb.gdt.jrivia.models.Game
import be.ehb.gdt.jrivia.models.Score
import be.ehb.gdt.jrivia.models.viewmodels.GameViewModel
import be.ehb.gdt.jrivia.models.viewmodels.ScoreViewModel
import be.ehb.gdt.jrivia.models.viewmodels.ScoreViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames
import com.google.android.material.snackbar.Snackbar

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val gameViewModel: GameViewModel by viewModels()
    private val scoreViewModel: ScoreViewModel by viewModels {
        ScoreViewModelFactory((application as JriviaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        // change behavior of the enter button of the keyboard
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
        binding.chronometer.setTextColor(binding.questionNumberTextView.currentTextColor)


        // data from intents
        val game: Game? = intent.getParcelableExtra(IntentExtraNames.GAME)


        if (game == null) {
            Snackbar.make(
                findViewById(R.id.gameLayout),
                getString(R.string.game_start_error),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.go_back) { finish() }
                .setActionTextColor(ContextCompat.getColor(this, R.color.secondaryLightColor))
                .show()

        } else {
            gameViewModel.game = game
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.start()
        }

        updateView()
    }

    private fun onNextClick() {
        gameViewModel.currentClue.guess = binding.answerEditText.text.trim().toString()
        if (gameViewModel.index < gameViewModel.game.numberOfQuestions - 1) {
            gameViewModel.moveNext()
            updateView()
        } else {
            gameViewModel.game.time = SystemClock.elapsedRealtime() - binding.chronometer.base
            scoreViewModel.insert(Score(gameViewModel.game))
            Intent(this, GameOverviewActivity::class.java)
                .apply {
                    putExtra(IntentExtraNames.GAME, gameViewModel.game)
                }
                .also { startActivity(it) }
        }
    }

    /** back button from the game itself */
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

    /** back button of the system */
    override fun onBackPressed() {
        // stop the chronometer
        binding.chronometer.stop()
        gameViewModel.pauseOffSet = SystemClock.elapsedRealtime() - binding.chronometer.base

        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.warning_message_end_game)
                .setTitle(R.string.warning_title_end_game)
                .setPositiveButton(R.string.end) { _, _ -> super.onBackPressed() }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface?, _ ->
                    dialog?.cancel()
                    binding.chronometer.base =
                        SystemClock.elapsedRealtime() - gameViewModel.pauseOffSet
                    binding.chronometer.start()
                }

            builder.create()
        }
        alertDialog.show()
    }
}