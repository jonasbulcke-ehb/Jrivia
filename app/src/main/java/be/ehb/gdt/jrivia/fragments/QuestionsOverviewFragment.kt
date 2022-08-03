package be.ehb.gdt.jrivia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import be.ehb.gdt.jrivia.adapters.QuestionAdapter
import be.ehb.gdt.jrivia.databinding.FragmentQuestionsOverviewBinding
import be.ehb.gdt.jrivia.models.viewmodels.GameViewModel


class QuestionsOverviewFragment : Fragment() {
    private var _binding: FragmentQuestionsOverviewBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionsOverviewBinding.inflate(inflater, container, false)

        binding.questionsRecyclerView.adapter = QuestionAdapter(requireContext(), gameViewModel.game.clues)
        binding.questionsRecyclerView.setHasFixedSize(true)

        return binding.root
    }


}