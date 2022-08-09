package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.databinding.ActivityDailyQuestDetailBinding
import be.ehb.gdt.jrivia.fragments.DailyQuestDetailFragment
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModelFactory
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames

class DailyQuestDetailActivity : FragmentActivity() {
    private lateinit var binding: ActivityDailyQuestDetailBinding
    private lateinit var viewPager: ViewPager2
    private val dailyQuestViewModel: DailyQuestViewModel by viewModels {
        DailyQuestViewModelFactory((application as JriviaApplication).dailyQuestRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDailyQuestDetailBinding.inflate(layoutInflater)

        binding.dailyQuestDetailToolbar.setNavigationOnClickListener { finish() }

        viewPager = binding.dailyQuestDetailPager

        dailyQuestViewModel.getQuestsOfLastMonth().observe(this) {
            viewPager.apply {
                adapter = DailyQuestDetailStateAdapter(this@DailyQuestDetailActivity, it)
                currentItem = intent.getIntExtra(IntentExtraNames.DAILY_QUEST_INDEX, 0)
                registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        binding.dailyQuestDetailToolbar.title = it[position].formattedDate
                    }
                })
            }
        }

        setContentView(binding.root)
    }

    private inner class DailyQuestDetailStateAdapter(fragment: FragmentActivity, val dataset: List<DailyQuest>) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount() = dataset.size

        override fun createFragment(position: Int): Fragment {
            val fragment = DailyQuestDetailFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(IntentExtraNames.DAILY_QUEST, dataset[position])
            }
            return fragment
        }
    }

}