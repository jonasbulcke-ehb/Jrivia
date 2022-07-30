package be.ehb.gdt.jrivia.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.ehb.gdt.jrivia.MainActivity
import be.ehb.gdt.jrivia.activities.GameLoadingActivity
import be.ehb.gdt.jrivia.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.btnSinglePlayer.setOnClickListener {
            startActivity(Intent(context, GameLoadingActivity::class.java))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}