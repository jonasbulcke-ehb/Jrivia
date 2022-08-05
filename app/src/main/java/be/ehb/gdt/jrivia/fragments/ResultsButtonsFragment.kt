package be.ehb.gdt.jrivia.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.ehb.gdt.jrivia.activities.MainActivity
import be.ehb.gdt.jrivia.activities.QuestionsOverviewActivity
import be.ehb.gdt.jrivia.activities.ScoreBoardActivity
import be.ehb.gdt.jrivia.databinding.FragmentResultsButtonsBinding
import be.ehb.gdt.jrivia.models.viewmodels.GameViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames

class ResultsButtonsFragment : Fragment() {
    private var _binding: FragmentResultsButtonsBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsButtonsBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.playAgainButton.setOnClickListener { activity?.finish() }
        binding.menuButton.setOnClickListener {
            Intent(context, MainActivity::class.java)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
                .also { startActivity(it) }
        }
        binding.viewQuestionsButton.setOnClickListener {
            Intent(context, QuestionsOverviewActivity::class.java)
                .apply { putExtra(IntentExtraNames.GAME, gameViewModel.game) }
                .also { startActivity(it) }
        }

        binding.overviewScoreboardButton.setOnClickListener {
            Intent(context, ScoreBoardActivity::class.java)
                .apply {
                    putExtra(
                        IntentExtraNames.NUMBER_OF_QUESTIONS,
                        gameViewModel.game.numberOfQuestions
                    )
                }
                .also { startActivity(it) }
        }

        return view
    }
}