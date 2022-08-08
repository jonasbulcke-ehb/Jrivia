package be.ehb.gdt.jrivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.adapters.DailyQuestsStateAdapter
import be.ehb.gdt.jrivia.databinding.FragmentDailyQuestsBinding
import com.google.android.material.tabs.TabLayoutMediator

class DailyQuestsFragment : Fragment() {
    private var _binding: FragmentDailyQuestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dailyQuestsStateAdapter: DailyQuestsStateAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyQuestsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dailyQuestsStateAdapter = DailyQuestsStateAdapter(this)
        viewPager = binding.dailyQuestViewPager
        viewPager.adapter = dailyQuestsStateAdapter

        val tabLayout = binding.dailyQuestTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.daily_quest_tab_names)[position]

            tab.icon = listOf(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_baseline_star_border_24
                ), ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_history_24)
            )[position]
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}