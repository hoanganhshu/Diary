package com.diary.app.demo.ui.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.diary.app.demo.R
import com.diary.app.demo.databinding.FragmentOnboard1Binding
import com.diary.app.demo.ui.onboarding.viewmodel.OnboardingViewModel
import com.google.android.material.card.MaterialCardView

class Onboarding1Fragment : Fragment() {
    private var _binding: FragmentOnboard1Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OnboardingViewModel by activityViewModels()
    private lateinit var options: List<MaterialCardView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboard1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        options = listOf(binding.op1, binding.op2, binding.op3, binding.op4)

        options.forEach { card ->
            card.setOnClickListener { selectOption(card) }
        }
    }

    private fun selectOption(selected: MaterialCardView) {
        options.forEach { card ->
            val isSelected = (card == selected)
            card.isChecked = isSelected
            updateCardStyle(card, isSelected)
        }
        sharedViewModel.setOptionSelected(true)
    }

    private fun updateCardStyle(card: MaterialCardView, isSelected: Boolean) {
        if (isSelected) {
            card.strokeColor = requireContext().getColor(R.color.purple_500)
            card.setCardBackgroundColor(requireContext().getColor(R.color.purple_500))
        } else {
            card.strokeColor = requireContext().getColor(R.color.gray_500)
            card.setCardBackgroundColor(requireContext().getColor(android.R.color.transparent))
        }
    }

    override fun onResume() {
        super.onResume()
        // Determine if any option is currently selected
        val hasSelection = options.any { it.isChecked }
        sharedViewModel.setOptionSelected(hasSelection)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}