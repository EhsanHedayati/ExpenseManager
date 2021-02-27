package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentDialogRoundedTwoBinding
import org.koin.android.ext.android.inject


class RoundedDialogTwo : DialogFragment() {
    lateinit var binding: FragmentDialogRoundedTwoBinding
    private val roundTwoViewModel: RoundedDialogTwoViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogRoundedTwoBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.roundedTwoModel = roundTwoViewModel
        binding.lifecycleOwner = this

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val title = RoundedDialogTwoArgs.fromBundle(requireArguments()).title
        roundTwoViewModel.title = title

        binding.yesButton.setOnClickListener {

            roundTwoViewModel.deleteExpense(title)
            roundTwoViewModel.deleteExpenseSpec(title)
            Toast.makeText(
                requireContext(),
                "با موفقیت حذف گردید",
                Toast.LENGTH_SHORT
            )
                .show()
            findNavController().navigate(RoundedDialogTwoDirections.actionRoundedDialogTwoToSettingFragment())

        }

        binding.noButton.setOnClickListener {
            findNavController().navigate(R.id.action_roundedDialogTwo_to_deleteIncomeExpenseFragment)


        }


    }


}