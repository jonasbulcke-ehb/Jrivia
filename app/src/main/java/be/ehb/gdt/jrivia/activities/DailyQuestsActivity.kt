package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.ActivityDailyQuestsBinding
import be.ehb.gdt.jrivia.fragments.DailyQuestsHistoryFragment
import be.ehb.gdt.jrivia.fragments.DailyQuestsTodayFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * FragmentActivity is used instead of AppCompatActivity, so this view can be passed to the adapter of the viewPager
 */
class DailyQuestsActivity : FragmentActivity() {
    private lateinit var binding: ActivityDailyQuestsBinding
    private lateinit var dailyQuestStateAdapter: DailyQuestsStateAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyQuestsBinding.inflate(layoutInflater)

        binding.dailyQuestToolbar.setNavigationOnClickListener { onNavigateUp() }

        dailyQuestStateAdapter = DailyQuestsStateAdapter(this)
        viewPager = binding.dailyQuestViewPager
        viewPager.adapter = dailyQuestStateAdapter

        // the tabLayout and viewPager are linked here, so that when the user swipes, the tabBar update and vice versa
        TabLayoutMediator(binding.dailyQuestTabLayout, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.daily_quest_tab_names)[position]

            tab.icon = listOf(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_star_border_24
                ), ContextCompat.getDrawable(this, R.drawable.ic_baseline_history_24)
            )[position]
        }.attach()

        val view = binding.root
        setContentView(view)
    }

    class DailyQuestsStateAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            // the fragments are already pre-made, as there is not one template that needs to be dynamically filled
            val fragments = listOf(DailyQuestsTodayFragment(), DailyQuestsHistoryFragment())
            return fragments[position]
        }
    }
}