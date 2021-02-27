package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentDialogRoundedBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dialog_rounded.*
import org.koin.android.ext.android.inject


class RoundedDialog : DialogFragment() {

    lateinit var binding: FragmentDialogRoundedBinding
    private val roundedViewModel: RoundedDialogViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogRoundedBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.dialogModel = roundedViewModel
        binding.lifecycleOwner = this

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val expense = RoundedDialogArgs.fromBundle(requireArguments()).expense
        roundedViewModel.expense = expense


        binding.yesButton.setOnClickListener {
            roundedViewModel.deleteExpense(expense)
            Toast.makeText(requireContext(), "با موفقیت حذف گردید", Toast.LENGTH_SHORT).show()
            findNavController().navigate(RoundedDialogDirections.actionRoundedDialogToDetailsFragment())

        }

        binding.noButton.setOnClickListener {

            findNavController().navigateUp()
        }


    }


}