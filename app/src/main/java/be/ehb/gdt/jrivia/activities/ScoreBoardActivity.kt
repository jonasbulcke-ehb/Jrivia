package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.adapters.ScoreListAdapter
import be.ehb.gdt.jrivia.databinding.ActivityScoreBoardBinding
import be.ehb.gdt.jrivia.models.viewmodels.ScoreViewModel
import be.ehb.gdt.jrivia.models.viewmodels.ScoreViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames

class ScoreBoardActivity : AppCompatActivity() {
    private val scoreViewModel: ScoreViewModel by viewModels {
        ScoreViewModelFactory((application as JriviaApplication).repository)
    }
    private lateinit var binding: ActivityScoreBoardBinding
    private val adapter = ScoreListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBoardBinding.inflate(layoutInflater)

        binding.questionsMinusButton.setOnClickListener { onNumberChangeButtonClicked(it) }
        binding.questionsPlusButton.setOnClickListener { onNumberChangeButtonClicked(it) }

        binding.scoresRecyclerView.adapter = adapter


        scoreViewModel.numberOfQuestions =
            intent.getIntExtra(IntentExtraNames.NUMBER_OF_QUESTIONS, 10)

        val view = binding.root
        supportActionBar?.title = getString(R.string.scoreboard)
        setContentView(view)

        updateView()
    }

    private fun updateView() {
        binding.questionsLabelTextView.text =
            getString(R.string.number_of_questions_with_number, scoreViewModel.numberOfQuestions)
        binding.questionsMinusButton.isEnabled = scoreViewModel.numberOfQuestions > 5
        binding.questionsPlusButton.isEnabled = scoreViewModel.numberOfQuestions < 25

        binding.scoresHeaderTotalTextView.isVisible = scoreViewModel.showAll
        binding.numberOfQuestionsLayout.isVisible = !scoreViewModel.showAll

        if (scoreViewModel.showAll) {
            scoreViewModel.allScores.observe(this) { scores ->
                scores?.let {
                    adapter.submitList(it)
                }
            }
        } else {
            scoreViewModel.getScoresByNumberOfQuestions(scoreViewModel.numberOfQuestions)
                .observe(this) { scores ->
                    scores?.let {
                        adapter.submitList(it)
                    }
                }
        }
//        adapter.listeners.forEach { it.onShowAllChanged(scoreViewModel.showAll) }
//        adapter.listener.onShowAllChanged(scoreViewModel.showAll)
    }

    private fun onNumberChangeButtonClicked(view: View) {
        when (view.id) {
            R.id.questionsMinusButton -> scoreViewModel.numberOfQuestions -= 5
            R.id.questionsPlusButton -> scoreViewModel.numberOfQuestions += 5
        }
        updateView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scoreboard_menu, menu)
        menu?.findItem(R.id.action_show_all)?.title =
            getString(if (scoreViewModel.showAll) R.string.show_per_number else R.string.show_all)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_show_all -> {
            scoreViewModel.showAll = !scoreViewModel.showAll
            adapter.onShowAllChanged(scoreViewModel.showAll)
            updateMenuItem(item)
            updateView()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun updateMenuItem(item: MenuItem) {
        item.title =
            getString(if (scoreViewModel.showAll) R.string.show_per_number else R.string.show_all)
        item.titleCondensed =
            getString(if (scoreViewModel.showAll) R.string.per_number else R.string.all)
    }


    interface NoticeShowAllListener {
        fun onShowAllChanged(showAll: Boolean)
    }
}