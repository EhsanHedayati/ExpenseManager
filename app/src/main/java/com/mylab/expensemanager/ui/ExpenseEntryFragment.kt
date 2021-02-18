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
import com.alirezaafkar.sundatepicker.DatePicker
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.IncomeExpenseTitleAdapter
import com.mylab.expensemanager.databinding.FragmentExpenseEntryBinding
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject

class ExpenseEntryFragment : Fragment() {

    private val TAG = "ExpenseEntryFragment"

    lateinit var binding: FragmentExpenseEntryBinding
    private val expenseEntryModel: ExpenseEntryViewModel by inject()
    var expenseSpec: ExpenseSpec? = null
    var title: String? = null
    private var dateMillie: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseEntryBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseEntryModel.fillExpenseSpinner.observe(viewLifecycleOwner) {

            binding.expenseEntrySpinner.adapter = IncomeExpenseTitleAdapter(requireContext(), it)
        }

        binding.dateExpenseEntry.setOnClickListener {

            activity?.let { it1 ->
                object : DatePicker.Builder() {}
                    .id(1)
                    .minDate(1390, 1, 1)
                    .maxDate(1420, 1, 1)
                    .date(1, 1, 1399)
                    .build { id, calendar, day, month, year ->

                        //binding.dateExpenseEntry.text = "$year / $month / $day"
                        dateMillie = calendar?.timeInMillis
                        binding.dateExpenseEntry.setText("$year / $month / $day")




                    }
                    .show(it1.supportFragmentManager, "")

            }


        }

        binding.expenseEntrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    expenseSpec = parent?.getItemAtPosition(position) as ExpenseSpec
                    title = expenseSpec?.title

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        binding.expenseEntryButton.setOnClickListener {

            val amount = binding.expenseEntryAmount.text.toString()
            val date = binding.dateExpenseEntry.text.toString()
            var desc = binding.expenseEntryDesc.text.toString()

            if (desc.isNullOrBlank()) {
                desc = ""
            }

            if ((title != "انتخاب گروه هزینه ای") && amount.isNotEmpty() && dateMillie != null) {
                val expense = Expense(0, title!!, amount.toLong(), dateMillie!!, desc, 0)
                expenseEntryModel.insertExpense(expense)
                Toast.makeText(requireContext(), "با موفقیت ذخیره گردید", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()

            } else {

                if (title == "انتخاب گروه هزینه ای") {
                    Toast.makeText(
                        requireContext(),
                        "گروه هزینه ای انتخاب گردد",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                if (amount.isEmpty()) {
                    Toast.makeText(requireContext(), "مبلغ هزینه درج گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (date.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "تاریخ هزینه درج گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            }
        }


    }

}