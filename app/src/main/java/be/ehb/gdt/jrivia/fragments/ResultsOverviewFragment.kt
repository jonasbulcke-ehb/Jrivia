package be.ehb.gdt.jrivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.ehb.gdt.jrivia.databinding.FragmentResultsOverviewBinding
import be.ehb.gdt.jrivia.viewmodels.GameViewModel

class ResultsOverviewFragment : Fragment() {
    private var _binding: FragmentResultsOverviewBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsOverviewBinding.inflate(inflater, container, false)

        binding.apply {
            gameOverviewCorrectQuestionsTextView.text =
                gameViewModel.game.clues.count { it.isCorrect }.toString()
            gameOverviewTotalQuestionsTextView.text =
                gameViewModel.game.numberOfQuestions.toString()
            gameOverviewTimeTextView.text = gameViewModel.game.formattedTime
            gameOverviewScoreTextView.text = gameViewModel.game.score.toString()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}