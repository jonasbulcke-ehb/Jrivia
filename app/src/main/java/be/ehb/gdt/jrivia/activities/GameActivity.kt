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
import be.ehb.gdt.jrivia.viewmodels.GameViewModel
import be.ehb.gdt.jrivia.viewmodels.ScoreViewModel
import be.ehb.gdt.jrivia.viewmodels.ScoreViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames
import com.google.android.material.snackbar.Snackbar

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val gameViewModel: GameViewModel by viewModels()
    private val scoreViewModel: ScoreViewModel by viewModels {
        ScoreViewModelFactory((application as JriviaApplication).scoreRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        // change the behavior of the enter button of the keyboard
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
            binding.chronometer.base = SystemClock.elapsedRealtime() // set the time of the chronometer to the current time
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
        binding.apply {
            questionNumberTextView.text =
                getString(R.string.question_number, (gameViewModel.index + 1))
            gameQuestionTextView.text = gameViewModel.currentClue.question
            answerEditText.setText(gameViewModel.currentClue.guess ?: "")
            questionsProgressIndicator.progress =
                ((gameViewModel.index.toDouble() / gameViewModel.game.numberOfQuestions) * 100).toInt()
            nextButton.text =
                if (gameViewModel.index >= gameViewModel.game.numberOfQuestions - 1) getString(R.string.finish)
                else getString(R.string.next)
            backButton.isEnabled = gameViewModel.index > 0
        }
    }

    /**
     * The behavior of the system's back button is overridden, where the chronometer is paused and
     * the activity prompts for confirmation if the game should be stopped
     */
    override fun onBackPressed() {
        binding.chronometer.stop() // this stops the chronometer, but only in the UI and not in the code behind it, that why a pauseOffSet will be stored
        gameViewModel.pauseOffSet = SystemClock.elapsedRealtime() - binding.chronometer.base // subtract the start time of the chronometer from the current time

        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.warning_message_end_game)
                .setTitle(R.string.warning_title_end_game)
                .setPositiveButton(R.string.end) { _, _ -> super.onBackPressed() }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface?, _ ->
                    dialog?.cancel()

                    // this sets the start time of the chronometer to the current time minus the pauseOffSet, this makes sure the pauseOffSet is shown in stead the current passed time
                    binding.chronometer.base =
                        SystemClock.elapsedRealtime() - gameViewModel.pauseOffSet
                    binding.chronometer.start()
                }
            builder.create()
        }
        alertDialog.show()
    }
}