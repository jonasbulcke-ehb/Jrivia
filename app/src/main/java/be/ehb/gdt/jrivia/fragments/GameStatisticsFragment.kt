package be.ehb.gdt.jrivia.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.ehb.gdt.jrivia.activities.GameActivity
import be.ehb.gdt.jrivia.activities.MainActivity
import be.ehb.gdt.jrivia.activities.QuestionOverviewActivity
import be.ehb.gdt.jrivia.databinding.FragmentGameStatisticsBinding
import be.ehb.gdt.jrivia.models.viewmodels.GameViewModel


class GameStatisticsFragment : Fragment() {
    private var _binding: FragmentGameStatisticsBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameStatisticsBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.gameOverviewCorrectQuestionsTextView.text =
            gameViewModel.game.clues.count { it.isCorrect() }.toString()
        binding.gameOverviewTotalQuestionsTextView.text =
            gameViewModel.game.numberOfQuestions.toString()
        binding.gameOverviewTimeTextView.text = gameViewModel.game.formattedTime
        binding.gameOverviewScoreTextView.text = gameViewModel.game.score.toString()

        binding.playAgainButton.setOnClickListener { activity?.finish() }
        binding.menuButton.setOnClickListener {
            Intent(context, MainActivity::class.java)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
                .also { startActivity(it) }
        }
        binding.viewQuestionsButton.setOnClickListener {
            Intent(context, QuestionOverviewActivity::class.java)
                .apply { putExtra(GameActivity.GAME, gameViewModel.game) }
                .also { startActivity(it) }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}