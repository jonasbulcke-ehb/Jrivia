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
import be.ehb.gdt.jrivia.models.Game
import be.ehb.gdt.jrivia.viewmodels.GameLoadingViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames
import com.google.android.material.snackbar.Snackbar

class GameLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameLoadingBinding
    private val loadingViewModel: GameLoadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameLoadingBinding.inflate(layoutInflater)
        val view = binding.root

        supportActionBar?.title = getString(R.string.single_player)

        binding.numberOfQuestionsSlider.addOnChangeListener { _, value, _ ->
            loadingViewModel.numberOfQuestions = value.toInt()
            updateView()
            fetchGame() // fetch a new game with the new number of questions
        }

        binding.usernameEditText.apply {
            addTextChangedListener {
                binding.startButton.isEnabled =
                    isUsernameValid() // enables the start button when the username is filled in
                loadingViewModel.username = binding.usernameEditText.text.trim()
                    .toString() // already stores the username in the viewModel
            }
            setText(loadingViewModel.username) // set the editText to the username if there is already one foreseen
        }

        binding.startButton.isEnabled =
            isUsernameValid() // enables the start button if there's already a username

        binding.startButton.setOnClickListener {
            loadingViewModel.saveUsername() // saves the username locally, so that the user does not always have to fill in the username field
            val game = Game(
                loadingViewModel.clues,
                loadingViewModel.username,
                loadingViewModel.numberOfQuestions
            )
            Intent(this, GameActivity::class.java)
                .apply {
                    putExtra(IntentExtraNames.GAME, game)
                }.also {
                    startActivity(it)
                }
        }
        setContentView(view)
    }

    private fun isUsernameValid(): Boolean {
        return binding.usernameEditText.text.isNotBlank() // checks if the username is filled in
    }

    // this method is overridden, so when the user come back at this loading activity, a new game is fetched
    override fun onResume() {
        super.onResume()
        binding.numberOfQuestionsSlider.value = loadingViewModel.numberOfQuestions.toFloat()
        fetchGame()
    }

    // when the fetching happens successfully
    private fun onSuccess() {
        binding.questionLoaderProgressIndicator.visibility = View.GONE
        binding.startButton.visibility = View.VISIBLE
    }

    // when there goes something wrong while fetching
    private fun onFailure() {
        binding.questionLoaderProgressIndicator.apply {
            isIndeterminate = false
            progress = 99
        }

        Snackbar.make(
            findViewById(R.id.loadingLayout),
            R.string.error_clues_fetching,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.go_back) { finish() }
            .setActionTextColor(
                ContextCompat.getColor(
                    this@GameLoadingActivity, R.color.secondaryLightColor
                )
            )
            .show()
    }

    private fun fetchGame() {
        binding.startButton.visibility = View.INVISIBLE
        binding.questionLoaderProgressIndicator.visibility = View.VISIBLE

        loadingViewModel.fetchClues(::onSuccess, ::onFailure)
    }

    private fun updateView() {
        binding.numberOfQuestionsLabelTextView.text =
            getString(R.string.number_of_questions_with_number, loadingViewModel.numberOfQuestions)
        binding.explanationTextView.text =
            getString(R.string.game_explanation, loadingViewModel.numberOfQuestions)
    }
}