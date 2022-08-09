package be.ehb.gdt.jrivia.fragments

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.input.InputManager
import android.os.Bundle
import android.os.IBinder
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.FragmentDailyQuestsTodayBinding
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.services.DailyQuestFetchService
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModel
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModelFactory
import kotlinx.coroutines.launch

class DailyQuestsTodayFragment : Fragment() {
    private var _binding: FragmentDailyQuestsTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DailyQuestViewModel by viewModels {
        DailyQuestViewModelFactory((requireActivity().application as JriviaApplication).dailyQuestRepository)
    }

    private lateinit var fetchService: DailyQuestFetchService
    private var serviceBound = false

//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
//            val binder = service as DailyQuestFetchService.DailyQuestFetchBinder
//            fetchService = binder.getService()
//            serviceBound = true
//        }
//
//        override fun onServiceDisconnected(p0: ComponentName?) {
//            serviceBound = false
//        }
//    }

//    override fun onStop() {
//        super.onStop()
//        activity?.unbindService(connection)
//        serviceBound = false
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyQuestsTodayBinding.inflate(inflater, container, false)

        viewModel.getLastQuest().observe(requireActivity()) {
            if(it == null) {
                binding.todayQuestLayout.isVisible = false
                binding.todayQuestProgressIndicator.isVisible = true
            } else {
                viewModel.lastQuest = it
                updateView(it)
            }
        }

        binding.solveAndAddPointsButton.setOnClickListener { onGuess() }

        binding.todayAnswerEditText.addTextChangedListener {
            binding.solveAndAddPointsButton.isEnabled = it!!.isNotBlank()
        }

        binding.todayAnswerEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onGuess()
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    private fun onGuess() {
        val isSolved = binding.todayAnswerEditText.text.trim().toString()
            .lowercase() == viewModel.lastQuest.answer.lowercase()
        viewModel.lastQuest.guesses++
        viewModel.lastQuest.isSolved = isSolved
        if (isSolved) {
            viewModel.updateLastQuest()

            binding.todayAnswerEditText.clearFocus()

            val inputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        updateView(viewModel.lastQuest)
    }

    private fun updateView(dailyQuest: DailyQuest) {
        binding.apply {
            todayQuestProgressIndicator.isVisible = false
            todayQuestLayout.isVisible = true
            if (dailyQuest.isSolved) {
                solveAndAddPointsButton.text = getString(R.string.points_earned, dailyQuest.value)
                todayGuessesTextView.text =
                    resources.getQuantityString(
                        R.plurals.guesses,
                        dailyQuest.guesses,
                        dailyQuest.guesses
                    )
                todayAnswerEditText.apply {
                    setText(dailyQuest.answer)
                    inputType = InputType.TYPE_NULL
                }
                isSolvedTextView.text = getString(R.string.have_solved)
                isSolvedTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryColor
                    )
                )
            } else {
                todayAnswerEditText.text.clear()
                solveAndAddPointsButton.text =
                    getString(R.string.solve_and_add_points_button, dailyQuest.value)
                todayGuessesTextView.text =
                    if (dailyQuest.guesses == 0) getString(R.string.zero_guesses_already)
                    else resources.getQuantityString(
                        R.plurals.guesses_already,
                        dailyQuest.guesses,
                        dailyQuest.guesses
                    )
                isSolvedTextView.text = getString(R.string.have_not_solved_yet)
                isSolvedTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryColor
                    )
                )
            }
            solveAndAddPointsButton.isEnabled = false
            todayQuestionTextView.text = dailyQuest.question
        }
    }

    override fun onPause() {
        super.onPause()
        if(binding.todayQuestLayout.isVisible && !viewModel.lastQuest.isSolved)
            viewModel.updateLastQuest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}