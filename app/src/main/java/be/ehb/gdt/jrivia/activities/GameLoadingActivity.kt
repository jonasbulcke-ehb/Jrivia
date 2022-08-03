package be.ehb.gdt.jrivia.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityGameLoadingBinding
import be.ehb.gdt.jrivia.models.Clue
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
    private lateinit var clues: Array<Clue>
    private val scope = MainScope()
    var numberOfQuestions = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameLoadingBinding.inflate(layoutInflater)
        val view = binding.root

        binding.numberOfQuestionsSlider.addOnChangeListener { _, value, _ ->
            numberOfQuestions = value.toInt()
            updateView()
            scope.launch { fetchGame() }
        }
        binding.startButton.setOnClickListener {
            Intent(this, GameActivity::class.java)
                .apply {
                    putExtra(EXTRA_CLUES, clues)
                    putExtra(NUMBER_OF_QUESTIONS, numberOfQuestions)
                }.also {
                    startActivity(it)
                }
        }

        setContentView(view)
    }

    override fun onResume() {
        super.onResume()

        binding.numberOfQuestionsSlider.value = numberOfQuestions.toFloat()

        scope.launch { fetchGame() }
    }

    private suspend fun fetchGame() {
        binding.startButton.visibility = View.INVISIBLE
        binding.questionLoaderProgressIndicator.visibility = View.VISIBLE
        withContext(Dispatchers.IO) {
            val url = "https://jservice.io/api/random?count=${numberOfQuestions}"
            val queue = Volley.newRequestQueue(this@GameLoadingActivity)
            val stringRequest = StringRequest(Request.Method.GET, url, {
                clues = Gson().fromJson(it, Array<Clue>::class.java)
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


    fun updateView() {
        binding.numberOfQuestionsLabelTextView.text = getString(R.string.number_of_questions, numberOfQuestions)
        binding.explanationTextView.text = getString(R.string.explanation, numberOfQuestions)
    }

    companion object {
        const val EXTRA_CLUES = "be.ehb.gdt.jrivia.GameLoadingActivity.EXTRA_CLUES"
        const val NUMBER_OF_QUESTIONS = "be.ehb.gdt.jrivia.GameLoadingActivity.NUMBER_OF_QUESTIONS"
    }
}