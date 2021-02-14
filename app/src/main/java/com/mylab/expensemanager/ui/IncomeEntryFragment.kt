package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.IncomeExpenseTitleAdapter
import com.mylab.expensemanager.databinding.FragmentIncomeEntryBinding
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.android.inject


class IncomeEntryFragment : Fragment() {

    lateinit var binding: FragmentIncomeEntryBinding
    private val incomeEntryModel: IncomeEntryViewModel by inject()
    private val persianCalendar = PersianCalendar()
    var expenseSpec: ExpenseSpec? = null
    var title: String? = null
    private var dateMillie: Long? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncomeEntryBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeEntryModel.fillIncomeSpinner.observe(viewLifecycleOwner) {

            binding.incomeEntrySpinner.adapter = IncomeExpenseTitleAdapter(requireContext(), it)

        }



        binding.incomeEntryDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->


                    dateMillie = persianCalendar.timeInMillis
                    val month = persianCalendar.persianMonthName
                    binding.incomeEntryDate.text = "$dayOfMonth $month $year"


                },
                persianCalendar.persianYear,
                persianCalendar.persianMonth,
                persianCalendar.persianDay
            )

            datePickerDialog.show(activity?.fragmentManager, "DatePickerDialog")

        }

        binding.incomeEntrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    expenseSpec = parent?.getItemAtPosition(position) as ExpenseSpec
                    //Toast.makeText( requireContext(), expenseSpec?.title, Toast.LENGTH_SHORT).show()
                    title = expenseSpec?.title


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        binding.incomeEntryButton.setOnClickListener {

            val amount = binding.incomeEntryAmount.text.toString()
            val date = binding.incomeEntryDate.text.toString()
            var desc = binding.incomeEntryDesc.text.toString()

            if (desc.isNullOrBlank()) {
                desc = ""
            }


            if ((title != "انتخاب گروه درآمدی") && amount.isNotEmpty() && dateMillie != null) {
                val expense = Expense(0, title!!, amount.toLong(), dateMillie!!, desc, 1)
                incomeEntryModel.insertExpense(expense)
                Toast.makeText(requireContext(), "با موفقیت ذخیره گردید", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()

            } else {

                if (title == "انتخاب گروه درآمدی") {
                    Toast.makeText(requireContext(), "گروه درآمدی انتخاب گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (amount.isEmpty()) {
                    Toast.makeText(requireContext(), "مبلغ درآمد درج گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (date.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "تاریخ درآمد درج گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }


            }


        }


    }


}