package be.ehb.gdt.jrivia.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.adapters.ScoreListAdapter
import be.ehb.gdt.jrivia.databinding.ActivityScoreBoardBinding
import be.ehb.gdt.jrivia.fragments.ConfirmScoreDeletionDialogFragment
import be.ehb.gdt.jrivia.models.Score
import be.ehb.gdt.jrivia.viewmodels.ScoreViewModel
import be.ehb.gdt.jrivia.viewmodels.ScoreViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames

class ScoreBoardActivity : AppCompatActivity(),
    ConfirmScoreDeletionDialogFragment.ConfirmScoreDeletionDialogListener {
    private val scoreViewModel: ScoreViewModel by viewModels {
        ScoreViewModelFactory((application as JriviaApplication).scoreRepository)
    }
    private lateinit var binding: ActivityScoreBoardBinding

    private var actionMode: ActionMode? = null // required for long clicks and a contextual app bar
    private var selectedScore: Score? = null

    private val adapter = ScoreListAdapter { this@ScoreBoardActivity.onScoreLongClick(it) }

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
        actionMode = startSupportActionMode(actionModeCallBack) // invokes the contextual app bar
        this.selectedScore = selectedScore // keep track of what score was long clicked
        return true
    }

    // callback for what to do with the selected score
    private val actionModeCallBack: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(
                R.menu.scoreboard_contextual_menu,
                menu
            ) // inflate the contextual app bar
            mode?.title = getString(R.string.score_selected)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        // listener for when a menu item is clicked of the contextual menu
        override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem?): Boolean {
            when (menuItem?.itemId) {
                R.id.action_share -> {
                    val shareText = selectedScore!!.run {
                        getString(
                            R.string.clue_to_string,
                            username,
                            value,
                            correctNumberOfQuestions,
                            totalNumberOfQuestions,
                            formattedTime
                        )
                    }
                    // creates a share intent, where the selected score is passed as a string
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    Intent.createChooser(shareIntent, getString(R.string.app_name)).also {
                        startActivity(it)
                    }
                    mode.finish()
                    return true
                }
                R.id.action_delete -> {
                    // prompts for confirmation if the score may be deleted
                    ConfirmScoreDeletionDialogFragment(this@ScoreBoardActivity)
                        .show(supportFragmentManager, "ConfirmScoreDeletionDialogFragment")
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
        binding.questionsMinusButton.isEnabled = scoreViewModel.numberOfQuestions > 5 // disables the minus button if the minimum is reached
        binding.questionsPlusButton.isEnabled = scoreViewModel.numberOfQuestions < 25 // disables the plus button if the max is reached

        binding.scoresHeaderTotalTextView.isVisible = scoreViewModel.showAll // hides the total column header
        binding.numberOfQuestionsLayout.isVisible = !scoreViewModel.showAll // hides the layout to change the number of questions

        // submit the list of scores based on what number of questions the user want to see
        if (scoreViewModel.showAll) {
            scoreViewModel.allScores.observe(this) {
                it?.let {
                    adapter.submitList(it)
                }
            }
        } else {
            scoreViewModel.getScoresByNumberOfQuestions(scoreViewModel.numberOfQuestions)
                .observe(this) {
                    it?.let {
                        adapter.submitList(it)
                    }
                }
        }
    }

    private fun onNumberChangeButtonClicked(view: View) {
        when (view.id) {
            R.id.questionsMinusButton -> scoreViewModel.numberOfQuestions -= 5
            R.id.questionsPlusButton -> scoreViewModel.numberOfQuestions += 5
        }
        updateView()
    }

    // show the menu items in the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.scoreboard_menu, menu)
        menu.findItem(R.id.action_show_all)?.title =
            getString(if (scoreViewModel.showAll) R.string.show_per_number else R.string.show_all)

        return super.onCreateOptionsMenu(menu)
    }

    // listener for when one of the actions are invoked of the app bar
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // show a dialog with explanation how the scores are ranked
        R.id.action_score_info -> {
            val explanationResId =
                if (scoreViewModel.showAll) R.string.score_explanation_all else R.string.score_explanation_per_number
            AlertDialog.Builder(this)
                .setMessage(getString(explanationResId))
                .setTitle(getString(R.string.explanation))
                .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create().show()

            true
        }
        // change the boolean whether the user wants to see all the score of just by number
        R.id.action_show_all -> {
            scoreViewModel.showAll = !scoreViewModel.showAll
            adapter.onShowAllChange(scoreViewModel.showAll) // let the adapter, that is a listener here, that the showAll boolean has changed
            item.title =
                getString(if (scoreViewModel.showAll) R.string.show_per_number else R.string.show_all)
            updateView()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    // implementation of the ConfirmScoreDeletionDialogListener, so that the score can be deleted on a positive button click
    override fun onDialogPositiveClick() {
        selectedScore?.let { scoreViewModel.delete(it) }
    }

    // implementation of the ConfirmScoreDeletionDialogListener, so that the contextual app bar can disappeared AFTER the dialog has dismissed
    override fun onDialogDismiss() {
        actionMode?.finish()
    }

    fun interface OnShowAllChangeListener {
        fun onShowAllChange(showAll: Boolean)
    }
}