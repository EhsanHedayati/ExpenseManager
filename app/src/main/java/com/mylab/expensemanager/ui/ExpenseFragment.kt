package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.databinding.FragmentExpenseBinding
import org.koin.android.ext.android.inject


class ExpenseFragment : Fragment() {
    lateinit var binding: FragmentExpenseBinding
    private val expenseModel: ExpenseViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.expenseRegisterButton.setOnClickListener {
            findNavController().navigate(
                ExpenseFragmentDirections.actionExpenseFragmentToExpenseEntryFragment(0)
            )


        }

        binding.expenseModel = expenseModel
        binding.lifecycleOwner = this


    }


}