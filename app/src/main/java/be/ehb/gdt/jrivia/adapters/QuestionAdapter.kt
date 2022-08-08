package be.ehb.gdt.jrivia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.RowQuestionBinding
import be.ehb.gdt.jrivia.models.Clue

class QuestionAdapter(private val clues: List<Clue>, private val context: Context) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val valueTextView: TextView = view.findViewById(R.id.valueTextView)
        private val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        private val answerTextView: TextView = view.findViewById(R.id.answerTextView)
        private val playersAnswerLabelTextView: TextView =
            view.findViewById(R.id.playersAnswerLabelTextView)
        private val playersAnswerTextView: TextView = view.findViewById(R.id.playersAnswerTextView)

        fun bind(clue: Clue) {
            val bgColor =
                if (clue.isCorrect) R.color.primaryLightColor else R.color.secondaryLightColor
            view.apply {
                setBackgroundColor(getColor(context, bgColor))
                background.alpha = 80
            }

            playersAnswerLabelTextView.isVisible = !clue.isCorrect
            playersAnswerTextView.isVisible = !clue.isCorrect

            valueTextView.text = clue.value.toString()
            questionTextView.text = clue.question
            answerTextView.text = clue.answer
            playersAnswerTextView.text = clue.guess
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(clues[position])
    }

    override fun getItemCount() = clues.size

}