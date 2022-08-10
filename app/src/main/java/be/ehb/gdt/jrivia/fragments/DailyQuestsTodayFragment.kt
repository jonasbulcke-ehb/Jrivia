package be.ehb.gdt.jrivia.fragments

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import be.ehb.gdt.jrivia.JriviaApplication
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.databinding.FragmentDailyQuestsTodayBinding
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.services.DailyQuestFetchService
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModel
import be.ehb.gdt.jrivia.viewmodels.DailyQuestViewModelFactory

class DailyQuestsTodayFragment : Fragment() {
    private var _binding: FragmentDailyQuestsTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DailyQuestViewModel by viewModels {
        DailyQuestViewModelFactory((requireActivity().application as JriviaApplication).dailyQuestRepository)
    }

    private var isReceiverRegistered = false // keeps track whether the receiver is registered
    private var broadcastReceiver: DailyQuestBroadcastReceiver? = null

    // overridden so the broadcast receiver can be registered
    override fun onResume() {
        super.onResume()
        // this receiver is used to let the UI know if there is a new daily quest and so the UI needs to be updated
        if (!isReceiverRegistered) {
            if (broadcastReceiver == null)
                broadcastReceiver = DailyQuestBroadcastReceiver()
            IntentFilter(DailyQuestFetchService.UPDATE_TODAY_QUEST_VIEW).also {
                activity?.registerReceiver(broadcastReceiver, it)
            }
            isReceiverRegistered = true
        }
    }

    // overridden so the broadcast receiver can be unregistered and that the today's quest only needs to be updated when the fragment is paused
    override fun onPause() {
        super.onPause()
        if (isReceiverRegistered) {
            activity?.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
            isReceiverRegistered = false
        }
        if (binding.todayQuestLayout.isVisible && !viewModel.lastQuest.isSolved)
            viewModel.updateLastQuest()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyQuestsTodayBinding.inflate(inflater, container, false)

        // bind the last quest to this view
        viewModel.getLastQuest().observe(requireActivity()) {
            if (it == null) {
                binding.todayQuestLayout.isVisible = false
                binding.questOfTodayUnavailableTextView.isVisible = true
            } else {
                viewModel.lastQuest = it
                updateView(it)
            }
        }

        binding.solveAndAddPointsButton.setOnClickListener { onGuess() }

        // disable the button if there is no answer filled in or if the last quest is not from today
        binding.todayAnswerEditText.addTextChangedListener {
            binding.solveAndAddPointsButton.isEnabled =
                it!!.isNotBlank() && viewModel.lastQuest.isFromToday()
        }

        // change the behavior of the enter button
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
        if (binding.todayAnswerEditText.text.isNotBlank()) {
            val isSolved = binding.todayAnswerEditText.text.trim().toString()
                .lowercase() == viewModel.lastQuest.answer.lowercase()
            viewModel.lastQuest.guesses++
            viewModel.lastQuest.isSolved = isSolved
            if (isSolved) {
                viewModel.updateLastQuest()

                binding.todayAnswerEditText.clearFocus()

                // let the keyboard disappear
                val inputMethodManager =
                    activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            }

            updateView(viewModel.lastQuest)
        }
    }

    private fun updateView(dailyQuest: DailyQuest) {
        fun setEnabledAnswerEditText(enabled: Boolean) {
            if (enabled) {
                binding.todayAnswerEditText.apply {
                    text.clear()
                    inputType = InputType.TYPE_CLASS_TEXT // makes the editText editable
                    isFocusableInTouchMode = true
                }
            } else {
                binding.todayAnswerEditText.apply {
                    setText(dailyQuest.answer)
                    inputType = InputType.TYPE_NULL
                    isFocusable = false
                }
            }
        }

        binding.apply {
            // hide the unavailable textview and show the other layout
            questOfTodayUnavailableTextView.isVisible = false
            todayQuestLayout.isVisible = true

            todayQuestionTextView.text = dailyQuest.question

            solveAndAddPointsButton.text = getString(
                if (dailyQuest.isSolved) R.string.points_earned else R.string.solve_and_add_points_button,
                dailyQuest.value
            )

            if (dailyQuest.isSolved) {
                todayGuessesTextView.text = resources.getQuantityString(
                    R.plurals.guesses, dailyQuest.guesses, dailyQuest.guesses
                )
                setIsSolvedTextView(R.string.have_solved, R.color.primaryColor)
                setEnabledAnswerEditText(false)
            } else if (dailyQuest.isFromToday()) {
                todayGuessesTextView.text =
                    if (dailyQuest.guesses == 0) getString(R.string.zero_guesses_already)
                    else resources.getQuantityString(
                        R.plurals.guesses_already,
                        dailyQuest.guesses,
                        dailyQuest.guesses
                    )
                setIsSolvedTextView(R.string.have_not_solved_yet, R.color.secondaryColor)
                setEnabledAnswerEditText(true)
            } else {
                todayGuessesTextView.text = resources.getQuantityString(
                    R.plurals.guesses, dailyQuest.guesses, dailyQuest.guesses
                )
                setIsSolvedTextView(R.string.missed_chance, R.color.secondaryColor)
                setEnabledAnswerEditText(false)
            }
            solveAndAddPointsButton.isEnabled = false
        }
    }

    private fun setIsSolvedTextView(stringResId: Int, colorResId: Int) {
        binding.isSolvedTextView.apply {
            text = getString(stringResId)
            setTextColor(
                ContextCompat.getColor(
                    requireContext(), colorResId
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class DailyQuestBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateView(viewModel.lastQuest)
        }
    }
}