package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentEditDeleteBinding


class DeleteIncomeExpenseFragment : Fragment() {
    lateinit var binding: FragmentEditDeleteBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDeleteBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}