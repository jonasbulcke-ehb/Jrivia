package be.ehb.gdt.jrivia.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.ehb.gdt.jrivia.activities.MainActivity
import be.ehb.gdt.jrivia.databinding.FragmentGameStatisticsBinding


class GameStatisticsFragment : Fragment() {
    private var _binding: FragmentGameStatisticsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameStatisticsBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.gameOverviewCorrectQuestionsTextView.text = "10"
        binding.gameOverviewTotalQuestionsTextView.text = "15"
        binding.gameOverviewTimeTextView.text = "02:43"
        binding.gameOverviewScoreTextView.text = "0"
        binding.playAgainButton.setOnClickListener { activity?.finish() }
        binding.menuButton.setOnClickListener {
            Intent(context, MainActivity::class.java)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
                .also { startActivity(it) }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}