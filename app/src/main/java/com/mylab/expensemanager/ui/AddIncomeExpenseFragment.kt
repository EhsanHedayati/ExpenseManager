package com.mylab.expensemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.ExtraIncomeExpenseAdapter
import com.mylab.expensemanager.databinding.FragmentAddIncomeExpenseBinding
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.android.inject


private const val TAG = "AddIncomeExpenseFragmen"

class AddIncomeExpenseFragment : Fragment() {


    lateinit var binding: FragmentAddIncomeExpenseBinding
    private val addExpenseIncomeViewModel: AddIncomeExpenseViewModel by inject()
    var selectedIncome: Int? = null
    var selectedExpense: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddIncomeExpenseBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseSpinnerSetup()
        incomeSpinnerSetup()

        binding.addExpense.setOnClickListener {
            val expenseCost = binding.expenseCost.text.toString().trim()
            val expenseType = 0
            val sum = 0L
            val image = selectedExpense
            if (expenseCost.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "عنوان گروه هزینه ای درج گردد", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            addExpenseIncomeViewModel.existenceExpenseSpec(expenseCost)
            addExpenseIncomeViewModel.expenseSpec.observe(viewLifecycleOwner) {
                Log.i(TAG, "onViewCreated: $it")

                if (it == null) {
                    val expenseSpec = ExpenseSpec(0, expenseCost, image!!, expenseType, sum)
                    addExpenseIncomeViewModel.insertExpenseSpec(expenseSpec)
                    Toast.makeText(
                        requireContext(),
                        "با موفقیت اضافه گردید",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()

                } else {
                    Toast.makeText(
                        context,
                        "گروه هزینه ای با این عنوان وجود دارد",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        binding.addIncome.setOnClickListener {
            val incomeCost = binding.incomeCost.text.toString().trim()
            val expenseType = 1
            val sum = 0L
            val image = selectedIncome
            if (incomeCost.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "عنوان گروه درآمدی درج گردد", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            addExpenseIncomeViewModel.existenceExpenseSpec(incomeCost)
            addExpenseIncomeViewModel.expenseSpec.observe(viewLifecycleOwner) {
                if (it == null) {
                    val expenseSpec = ExpenseSpec(0, incomeCost, image!!, expenseType, sum)
                    addExpenseIncomeViewModel.insertExpenseSpec(expenseSpec)
                    Toast.makeText(requireContext(), "با موفقیت اضافه گردید", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()

                } else {
                    Toast.makeText(
                        context,
                        "گروه درآمدی با این عنوان وجود دارد",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

        }

        binding.extraIncomeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedIncome = parent?.getItemAtPosition(position) as Int?
                    /*Toast.makeText(requireContext(), selectedIncome.toString(), Toast.LENGTH_SHORT)
                        .show()*/
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        binding.extraExpenseSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedExpense = parent?.getItemAtPosition(position) as Int?
                    /*Toast.makeText(requireContext(), selectedExpense.toString(), Toast.LENGTH_SHORT)
                        .show()*/
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

    }

    private fun expenseSpinnerSetup() {
        val expenseAdapter =
            ExtraIncomeExpenseAdapter(requireContext(), addExpenseIncomeViewModel.getExtraExpense())
        binding.extraExpenseSpinner.adapter = expenseAdapter

    }

    private fun incomeSpinnerSetup() {
        val incomeAdapter =
            ExtraIncomeExpenseAdapter(requireContext(), addExpenseIncomeViewModel.getExtraIncome())
        binding.extraIncomeSpinner.adapter = incomeAdapter

    }


}