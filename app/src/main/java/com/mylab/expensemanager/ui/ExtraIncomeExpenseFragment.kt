package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylab.expensemanager.databinding.FragmentExtraIncomeExpenseBinding


class ExtraIncomeExpenseFragment : Fragment() {
    lateinit var binding: FragmentExtraIncomeExpenseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExtraIncomeExpenseBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}