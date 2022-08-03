package be.ehb.gdt.jrivia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.ehb.gdt.jrivia.R


/**
 * A simple [Fragment] subclass.
 * Use the [QuestionsOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionsOverviewFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions_overview, container, false)
    }


}