package be.ehb.gdt.jrivia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.models.Clue

class QuestionAdapter(private val context: Context, private val clues: List<Clue>) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(private val context: Context, private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val valueTextView: TextView = view.findViewById(R.id.valueTextView)
        private val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        private val answerTextView: TextView = view.findViewById(R.id.answerTextView)
        private val playersAnswerLabelTextView: TextView =
            view.findViewById(R.id.playersAnswerLabelTextView)
        private val playersAnswerTextView: TextView = view.findViewById(R.id.playersAnswerTextView)

        fun bind(clue: Clue) {
            val bgColor =
                if (clue.isCorrect()) R.color.primaryLightColor else R.color.secondaryLightColor
            view.setBackgroundColor(getColor(context, bgColor))
            view.background.alpha = 80

            val playersAnswerVisibility = if (clue.isCorrect()) View.GONE else View.VISIBLE
            playersAnswerLabelTextView.visibility = playersAnswerVisibility
            playersAnswerTextView.visibility = playersAnswerVisibility

            valueTextView.text = clue.value.toString()
            questionTextView.text = clue.question
            answerTextView.text = clue.answer
            playersAnswerTextView.text = clue.guess
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_row, parent, false)

        return QuestionViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(clues[position])
    }

    override fun getItemCount() = clues.size

}