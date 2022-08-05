package be.ehb.gdt.jrivia.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityGameLoadingBinding
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.models.Game
import be.ehb.gdt.jrivia.models.viewmodels.GameLoadingViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GameLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameLoadingBinding
    private val scope = MainScope()
    private val loadingViewModel: GameLoadingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameLoadingBinding.inflate(layoutInflater)
        val view = binding.root

        binding.numberOfQuestionsSlider.addOnChangeListener { _, value, _ ->
            loadingViewModel.numberOfQuestions = value.toInt()
            updateView()
            scope.launch { fetchGame() }
        }

        binding.startButton.setOnClickListener {
            Intent(this, GameActivity::class.java)
                .apply {
                    val game = Game(
                        loadingViewModel.clues,
                        loadingViewModel.username,
                        loadingViewModel.numberOfQuestions
                    )
                    putExtra(IntentExtraNames.GAME, game)
                }.also {
                    startActivity(it)
                }
        }

        binding.usernameEditText.addTextChangedListener {
            binding.startButton.isEnabled = binding.usernameEditText.text.isNotBlank()
            loadingViewModel.username = binding.usernameEditText.text.trim().toString()
        }

        setContentView(view)
    }

    override fun onResume() {
        super.onResume()

        binding.numberOfQuestionsSlider.value = loadingViewModel.numberOfQuestions.toFloat()

        scope.launch { fetchGame() }
    }

    private fun onSuccess() {
        binding.questionLoaderProgressIndicator.visibility = View.GONE
        binding.startButton.visibility = View.VISIBLE
    }

    private fun onFailure() {
        binding.questionLoaderProgressIndicator.isIndeterminate = false
        binding.questionLoaderProgressIndicator.progress = 99
        Snackbar.make(
            findViewById(R.id.loadingLayout),
            R.string.error_clues_fetching,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.go_back) { finish() }
            .setActionTextColor(
                ContextCompat.getColor(
                    this@GameLoadingActivity, R.color.secondaryLightColor
                )
            )
            .show()
    }

    private suspend fun fetchGame() {
        binding.startButton.visibility = View.INVISIBLE
        binding.questionLoaderProgressIndicator.visibility = View.VISIBLE

        loadingViewModel.fetchClues({ onSuccess() }, { onFailure() })
    }


    private fun updateView() {
        binding.numberOfQuestionsLabelTextView.text =
            getString(R.string.number_of_questions_with_number, loadingViewModel.numberOfQuestions)
        binding.explanationTextView.text =
            getString(R.string.explanation, loadingViewModel.numberOfQuestions)
    }
}