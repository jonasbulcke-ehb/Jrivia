package be.ehb.gdt.jrivia.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityGameLoadingBinding
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.models.Game
import be.ehb.gdt.jrivia.util.IntentExtraNames
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameLoadingBinding
    private val scope = MainScope()
    private val game = Game(ArrayList(), "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameLoadingBinding.inflate(layoutInflater)
        val view = binding.root

        binding.numberOfQuestionsSlider.addOnChangeListener { _, value, _ ->
            game.numberOfQuestions = value.toInt()
            updateView()
            scope.launch { fetchGame() }
        }

        binding.startButton.setOnClickListener {
            Intent(this, GameActivity::class.java)
                .apply {
                    putExtra(IntentExtraNames.GAME, game)
                }.also {
                    startActivity(it)
                }
        }

        binding.usernameEditText.addTextChangedListener {
            binding.startButton.isEnabled = binding.usernameEditText.text.isNotBlank()
            game.username = binding.usernameEditText.text.trim().toString()
        }

        setContentView(view)
    }

    override fun onResume() {
        super.onResume()

        binding.numberOfQuestionsSlider.value = game.numberOfQuestions.toFloat()

        scope.launch { fetchGame() }
    }

    private suspend fun fetchGame() {
        binding.startButton.visibility = View.INVISIBLE
        binding.questionLoaderProgressIndicator.visibility = View.VISIBLE
        withContext(Dispatchers.IO) {
            val url = "https://jservice.io/api/random?count=${game.numberOfQuestions}"
            val queue = Volley.newRequestQueue(this@GameLoadingActivity)
            val stringRequest = StringRequest(Request.Method.GET, url, {
                game.clues = Gson().fromJson(it, Array<Clue>::class.java).toList()
                binding.questionLoaderProgressIndicator.visibility = View.GONE
                binding.startButton.visibility = View.VISIBLE
            }, {
                binding.questionLoaderProgressIndicator.isIndeterminate = false
                binding.questionLoaderProgressIndicator.progress = 99
                Snackbar.make(
                    findViewById(R.id.loadingLayout),
                    R.string.error_clues_fetching,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.go_back) {
                    finish()
                }
                    .setActionTextColor(ContextCompat.getColor(this@GameLoadingActivity, R.color.secondaryLightColor))
                    .show()
            })
            queue.add(stringRequest)
        }
    }


    private fun updateView() {
        binding.numberOfQuestionsLabelTextView.text = getString(R.string.number_of_questions_with_number, game.numberOfQuestions)
        binding.explanationTextView.text = getString(R.string.explanation, game.numberOfQuestions)
    }
}