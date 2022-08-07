package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.adapters.ScoreListAdapter
import be.ehb.gdt.jrivia.databinding.ActivityScoreBoardBinding
import be.ehb.gdt.jrivia.fragments.ConfirmScoreDeletionDialogFragment
import be.ehb.gdt.jrivia.models.Score
import be.ehb.gdt.jrivia.models.viewmodels.ScoreViewModel
import be.ehb.gdt.jrivia.models.viewmodels.ScoreViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames

class ScoreBoardActivity : AppCompatActivity(),
    ConfirmScoreDeletionDialogFragment.ConfirmScoreDeletionDialogListener {
    private val scoreViewModel: ScoreViewModel by viewModels {
        ScoreViewModelFactory((application as JriviaApplication).scoreRepository)
    }
    private lateinit var binding: ActivityScoreBoardBinding

    private var actionMode: ActionMode? = null
    private var selectedScore: Score? = null

    private val adapter = ScoreListAdapter(object : ScoreListAdapter.OnScoreLongClickListener {
        override fun onScoreLongClick(score: Score): Boolean {
            return this@ScoreBoardActivity.onScoreLongClick(score)
        }
    })

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

    private fun onScoreLongClick(selectedScore: Score): Boolean {
        if (actionMode != null)
            return false
        actionMode = startSupportActionMode(actionModeCallBack)
        this.selectedScore = selectedScore
        return true
    }

    private val actionModeCallBack: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.scoreboard_contextual_menu, menu)
            mode?.title = "Score selected"
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onActionItemClicked(mode: ActionMode?, menuItem: MenuItem?): Boolean {
            when (menuItem?.itemId) {
                R.id.action_share -> {
                    Toast.makeText(
                        this@ScoreBoardActivity, "Share option selected",
                        Toast.LENGTH_SHORT
                    ).show()
                    mode?.finish()
                    return true
                }
                R.id.action_delete -> {
                    val dialog = ConfirmScoreDeletionDialogFragment(this@ScoreBoardActivity)
                    dialog.show(supportFragmentManager, "ConfirmScoreDeletionDialogFragment")
                    return true
                }
                else -> return false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            selectedScore = null
        }
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

    override fun onDialogPositiveClick(dialog: DialogFragment) {
         selectedScore?.let { scoreViewModel.delete(it); Log.d("DELETE", "SUCCESS") }
    }

    override fun onDialogDismiss() {
        actionMode?.finish()
    }


    interface NoticeShowAllListener {
        fun onShowAllChanged(showAll: Boolean)
    }
}