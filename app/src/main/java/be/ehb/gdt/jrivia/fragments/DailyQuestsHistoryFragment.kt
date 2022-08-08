package be.ehb.gdt.jrivia.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.activities.DailyQuestDetailActivity
import be.ehb.gdt.jrivia.adapters.DailyQuestAdapter
import be.ehb.gdt.jrivia.databinding.FragmentDailyQuestsHistoryBinding
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.models.viewmodels.DailyQuestViewModel
import be.ehb.gdt.jrivia.models.viewmodels.DailyClueViewModelFactory
import be.ehb.gdt.jrivia.util.IntentExtraNames

class DailyQuestsHistoryFragment : Fragment(), DailyQuestAdapter.OnDailyQuestClickListener {
    private var _binding: FragmentDailyQuestsHistoryBinding? = null
    private val binding get() = _binding!!
    private val dailyQuestViewModel: DailyQuestViewModel by viewModels {
        DailyClueViewModelFactory((requireActivity().application as JriviaApplication).dailyQuestRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyQuestsHistoryBinding.inflate(layoutInflater)

        dailyQuestViewModel.getQuestsOfLastMonth().observe(requireActivity()) {
            binding.dailyQuestsHistoryRecyclerView.apply {
                adapter = DailyQuestAdapter(it) { onDailyQuestClick(it) }
                setHasFixedSize(true)
            }
        }

        return binding.root
    }

    override fun onDailyQuestClick(dailyQuest: DailyQuest) {
        Intent(activity, DailyQuestDetailActivity::class.java)
            .apply { putExtra(IntentExtraNames.DAILY_QUEST, dailyQuest) }
            .also { startActivity(it) }
    }


}