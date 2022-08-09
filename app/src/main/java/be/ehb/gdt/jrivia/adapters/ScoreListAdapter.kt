package be.ehb.gdt.jrivia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.activities.ScoreBoardActivity
import be.ehb.gdt.jrivia.models.Score

/**
 * This adapter inherits from the ListAdapter in stead of the RecyclerView.Adapter, because the dataset list can change a lot
 *
 * @property onLongScoreClickListener the listener for long clicks that will be assigned to each view holder
 */
class ScoreListAdapter(private val onLongScoreClickListener: OnScoreLongClickListener) :
    ListAdapter<Score, ScoreListAdapter.ScoreViewHolder>(ScoreComparator()),
    ScoreBoardActivity.OnShowAllChangeListener {
    private var showAll = false // keeps track if all number of questions should be shown
    private val holders = ArrayList<ScoreViewHolder>() // list of all the viewHolders that needs to be aware of the changing showAll boolean

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val holder = ScoreViewHolder.create(parent)
        holders.add(holder) // the holder is added to the list of holder when the holder is created
        return holder
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, showAll, onLongScoreClickListener)
    }

    class ScoreViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val usernameTextView: TextView = view.findViewById(R.id.usernameTextView)
        private val scoreValueTextView: TextView = view.findViewById(R.id.scoreValueTextView)
        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        private val correctTextView: TextView = view.findViewById(R.id.correctQuestionsTextView)
        private val totalTextView: TextView = view.findViewById(R.id.totalQuestionsTextView)

        fun bind(
            score: Score,
            showAll: Boolean,
            onLongScoreClickListener: OnScoreLongClickListener
        ) {
            usernameTextView.text = score.username
            scoreValueTextView.text = score.value.toString()
            timeTextView.text = score.formattedTime
            correctTextView.text = score.correctNumberOfQuestions.toString()
            totalTextView.text = score.totalNumberOfQuestions.toString()
            view.setOnLongClickListener { onLongScoreClickListener.onScoreLongClick(score)}
            totalTextView.isVisible = showAll
        }

        fun setTotalVisibility(isVisibility: Boolean) {
            totalTextView.isVisible = isVisibility
        }

        companion object {
            fun create(parent: ViewGroup): ScoreViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_score, parent, false)

                return ScoreViewHolder(view)
            }
        }
    }

    class ScoreComparator : DiffUtil.ItemCallback<Score>() {
        override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * implementation of the OnShowAllChangedListener
     */
    override fun onShowAllChange(showAll: Boolean) {
        this.showAll = showAll
        holders.forEach { it.setTotalVisibility(showAll) } // notify all the view holder to change the visibility of the total column
    }

    fun interface OnScoreLongClickListener {
        fun onScoreLongClick(score: Score): Boolean
    }
}
