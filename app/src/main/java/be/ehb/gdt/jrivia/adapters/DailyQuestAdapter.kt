package be.ehb.gdt.jrivia.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.models.DailyQuest

class DailyQuestAdapter(
    private val dataset: List<DailyQuest>,
    val context: Context,
    private val onDailyQuestClickListener: OnDailyQuestClickListener
) : RecyclerView.Adapter<DailyQuestAdapter.DailyQuestViewHolder>() {

    inner class DailyQuestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        private val solvedInGuessesTextView: TextView =
            view.findViewById(R.id.solvedInGuessesTextView)
        private val isSolvedCheckBox: CheckBox = view.findViewById(R.id.isSolvedCheckBox)
        private val questionTextView: TextView =
            view.findViewById(R.id.dailyQuestRowQuestionTextView)

        @SuppressLint("SimpleDateFormat")
        fun bind(dailyQuest: DailyQuest) {
            dateTextView.text = dailyQuest.formattedDate
            solvedInGuessesTextView.text =
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    dailyQuest.guesses.toString()
                else
                    context.resources.getQuantityString(
                        R.plurals.guesses, dailyQuest.guesses, dailyQuest.guesses
                    )

            isSolvedCheckBox.isChecked = dailyQuest.isSolved
            questionTextView.text = dailyQuest.question
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyQuestViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_daily_quest, parent, false)
        return DailyQuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyQuestViewHolder, position: Int) {
        holder.bind(dataset[position])
        holder.itemView.setOnClickListener { onDailyQuestClickListener.onDailyQuestClick(position) }
    }

    override fun getItemCount() = dataset.size

    fun interface OnDailyQuestClickListener {
        fun onDailyQuestClick(position: Int)
    }
}