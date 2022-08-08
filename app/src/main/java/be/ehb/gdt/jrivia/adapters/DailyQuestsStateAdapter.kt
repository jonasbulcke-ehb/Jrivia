package be.ehb.gdt.jrivia.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import be.ehb.gdt.jrivia.fragments.DailyQuestsHistoryFragment
import be.ehb.gdt.jrivia.fragments.DailyQuestsTodayFragment

class DailyQuestsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragments = listOf(DailyQuestsTodayFragment(), DailyQuestsHistoryFragment())
        return fragments[position]
    }
}