package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addGroup.setOnClickListener {

            findNavController().navigate(R.id.action_settingFragment_to_addIncomeExpenseFragment)

        }

        binding.deleteGroup.setOnClickListener {

            findNavController().navigate(R.id.action_settingFragment_to_deleteIncomeExpenseFragment)

        }
    }


}