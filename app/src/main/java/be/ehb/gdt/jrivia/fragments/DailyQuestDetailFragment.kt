package be.ehb.gdt.jrivia.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.FragmentDailyQuestDetailBinding
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.util.IntentExtraNames

class DailyQuestDetailFragment : Fragment() {
    private var _binding: FragmentDailyQuestDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyQuestDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(IntentExtraNames.DAILY_QUEST) }?.apply {
            val dailyQuest: DailyQuest = get(IntentExtraNames.DAILY_QUEST) as DailyQuest
            binding.dailyQuestDetailQuestionTextView.text = dailyQuest.question
            binding.solvedTextView.apply {
                text = getString(if (dailyQuest.isSolved) R.string.solved else R.string.unsolved)
                setTextColor(getTextColorByDailyQuest(dailyQuest))
            }
            binding.guessesTextview.apply {
                val pluralResId =
                    if (dailyQuest.isSolved) R.plurals.in_guesses else R.plurals.guesses
                text = resources.getQuantityString(
                    pluralResId,
                    dailyQuest.guesses,
                    dailyQuest.guesses
                )
                setTextColor(getTextColorByDailyQuest(dailyQuest))
            }

            binding.pointsTextView.text = dailyQuest.value.toString()

            if (dailyQuest.isSolved || !dailyQuest.isFromToday()) {
                binding.detailAnswerTextView.text = dailyQuest.answer
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTextColorByDailyQuest(dailyQuest: DailyQuest) = ContextCompat.getColor(
        requireContext(),
        if (dailyQuest.isSolved) R.color.primaryColor else R.color.secondaryColor
    )

}