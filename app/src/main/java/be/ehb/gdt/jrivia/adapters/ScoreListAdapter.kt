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

class ScoreListAdapter : ListAdapter<Score, ScoreListAdapter.ScoreViewHolder>(ScoreComparator()), ScoreBoardActivity.NoticeShowAllListener {
    private var showAll = false
    private val holders = ArrayList<ScoreViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val holder =  ScoreViewHolder.create(parent)
        holders.add(holder)
        return holder
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, showAll)
    }

    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val usernameTextView: TextView = view.findViewById(R.id.usernameTextView)
        private val scoreValueTextView: TextView = view.findViewById(R.id.scoreValueTextView)
        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        private val correctTextView: TextView = view.findViewById(R.id.correctQuestionsTextView)
        private val totalTextView: TextView = view.findViewById(R.id.totalQuestionsTextView)

        fun bind(score: Score, showAll: Boolean) {
            usernameTextView.text = score.username
            scoreValueTextView.text = score.value.toString()
            timeTextView.text = score.formattedTime
            correctTextView.text = score.correctNumberOfQuestions.toString()
            totalTextView.text = score.totalNumberOfQuestions.toString()


            totalTextView.isVisible = showAll
        }

        fun setVisibility(isVisibility: Boolean) {
            totalTextView.isVisible = isVisibility
        }

        companion object {
            fun create(parent: ViewGroup): ScoreViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.score_row, parent, false)
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

    override fun onShowAllChanged(showAll: Boolean) {
        this.showAll = showAll
        holders.forEach { it.setVisibility(showAll) }
    }
}