package be.ehb.gdt.jrivia.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.activities.DailyQuestDetailActivity
import be.ehb.gdt.jrivia.adapters.DailyQuestAdapter
import be.ehb.gdt.jrivia.databinding.FragmentDailyQuestsHistoryBinding
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModel
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames

class DailyQuestsHistoryFragment : Fragment(), DailyQuestAdapter.OnDailyQuestClickListener {
    private var _binding: FragmentDailyQuestsHistoryBinding? = null
    private val binding get() = _binding!!
    private val dailyQuestViewModel: DailyQuestViewModel by viewModels {
        DailyQuestViewModelFactory((requireActivity().application as JriviaApplication).dailyQuestRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyQuestsHistoryBinding.inflate(layoutInflater)

        dailyQuestViewModel.getQuestsOfLastMonth().observe(requireActivity()) { dailyQuests ->

            binding.apply {
                dailyQuestsHistoryRecyclerView.apply {
                    adapter = DailyQuestAdapter(dailyQuests, requireContext()) { onDailyQuestClick(it) }
                    setHasFixedSize(true)
                }
                totalPointsTextView.text =
                    dailyQuests.filter { it.isSolved }.sumOf { it.value }.toString()

                val solvedQuests = dailyQuests.count { it.isSolved }
                solvedQuestsTextView.text = solvedQuests.toString()
                solvedQuestsLabelTextView.text = getString(R.string.solved_quests)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDailyQuestClick(position: Int) {
        Intent(activity, DailyQuestDetailActivity::class.java)
            .apply { putExtra(IntentExtraNames.DAILY_QUEST_INDEX, position) }
            .also { startActivity(it) }
    }


}