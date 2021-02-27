package com.mylab.expensemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentMainBinding
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.util.firsDayOfWeek
import com.mylab.expensemanager.util.getPersianDate
import com.mylab.expensemanager.util.getToday
import org.koin.android.ext.android.inject

private const val TAG = "MainFragment"

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private val incomeExpenseSpec = mutableListOf<ExpenseSpec>()
    private val mainViewModel: MainViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainModel = mainViewModel
        binding.lifecycleOwner = this




        val callBack = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                findNavController().navigate(MainFragmentDirections.actionMainFragmentToRoundedDialogThree())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)




        mainViewModel.expenseSpecCount.observe(viewLifecycleOwner) {

            if (it.isEmpty()) {
                mainViewModel.insertAllExpenseSpec(getIncomeExpenseSpecList())
            }
        }

        binding.view1.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_incomeFragment)
        }
        binding.view2.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_expenseFragment)
        }

        binding.view3.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingFragment)

        }
        binding.view4.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment)
        }


    }

    private fun getIncomeExpenseSpecList(): List<ExpenseSpec> {

        incomeExpenseSpec.add(ExpenseSpec(0, "انتخاب گروه درآمدی", R.drawable.ic_profit, 1))
        incomeExpenseSpec.add(ExpenseSpec(0, "پاداش", R.drawable.ic_icon_bonus, 1))
        incomeExpenseSpec.add(ExpenseSpec(0, "سود سهام", R.drawable.ic_icon_dividend, 1))
        incomeExpenseSpec.add(ExpenseSpec(0, "سود بانکی", R.drawable.ic_icon_interest, 1))
        incomeExpenseSpec.add(ExpenseSpec(0, "حقوق", R.drawable.ic_icon_pay, 1))
        incomeExpenseSpec.add(ExpenseSpec(0, "دستمزد", R.drawable.ic_icon_salary, 1))
        incomeExpenseSpec.add(ExpenseSpec(0, "یارانه", R.drawable.ic_icon_subsidy, 1))

        incomeExpenseSpec.add(ExpenseSpec(0, "انتخاب گروه هزینه ای", R.drawable.ic_icon_expense, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "مسکن", R.drawable.ic_icon_home, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "خوراک", R.drawable.ic_icon_food, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "حمل و نقل", R.drawable.ic_icon_transport, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "وسیله نقلیه", R.drawable.ic_icon_vehicle, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "تفریحات", R.drawable.ic_icon_entertainment, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "ارتباطات", R.drawable.ic_icon_communication, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "پوشاک", R.drawable.ic_icon_clothing, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "درمان", R.drawable.ic_icon_treatment, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "پس انداز", R.drawable.ic_icon_money_box, 0))
        incomeExpenseSpec.add(ExpenseSpec(0, "سرمایه گذاری", R.drawable.ic_icon_investment, 0))

        return incomeExpenseSpec
    }


}