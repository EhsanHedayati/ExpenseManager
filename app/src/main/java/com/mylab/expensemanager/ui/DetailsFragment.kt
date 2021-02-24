package com.mylab.expensemanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.alirezaafkar.sundatepicker.DatePicker
import com.mylab.expensemanager.DetailsListAdapter
import com.mylab.expensemanager.IncomeExpenseTitleAdapter
import com.mylab.expensemanager.databinding.FragmentDetailsBinding
import com.mylab.expensemanager.datamodel.DetailsData
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.android.inject
import java.util.*


private const val TAG = "DetailsFragment"

class DetailsFragment : Fragment() {

    lateinit var binding: FragmentDetailsBinding
    private val detailsViewModel: DetailsViewModel by inject()
    private var endMillie: Long? = null
    private var startMillie: Long? = null
    private var amountType: Int? = null
    var expenseSpec: ExpenseSpec? = null
    var title: String? = null
    private val detailsAdapter = DetailsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.detailsModel = detailsViewModel
        binding.lifecycleOwner = this


        binding.detailsRecycler.apply {
            adapter = detailsAdapter
            var itemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
        }

        detailsViewModel.detailsList.observe(viewLifecycleOwner) {

            detailsAdapter.submitList(it)
            Log.i(TAG, "onViewCreated: $it")
        }


        detailsAdapter.itemClick = { detailsData ->
            findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToEditDeleteFragment(
                    detailsData
                )
            )
        }



        detailsViewModel.amountType.observe(viewLifecycleOwner) {

            amountType = it

            Log.i(TAG, "onViewCreated: $it")
            if (it == 0) {
                binding.incomeSpinner.setSelection(0)
                binding.incomeSpinner.isEnabled = false
                binding.expenseSpinner.isEnabled = true
            }
            if (it == 1) {
                binding.expenseSpinner.setSelection(0)
                binding.expenseSpinner.isEnabled = false
                binding.incomeSpinner.isEnabled = true
            }


        }

        binding.expenseSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    expenseSpec = parent?.getItemAtPosition(position) as ExpenseSpec
                    title = expenseSpec?.title
                    Log.i(TAG, "onItemSelected: $title")

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        binding.incomeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    expenseSpec = parent?.getItemAtPosition(position) as ExpenseSpec
                    title = expenseSpec?.title
                    Log.i(TAG, "onItemSelected: $title")

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }


        detailsViewModel.fillIncomeSpinner.observe(viewLifecycleOwner) {

            binding.incomeSpinner.adapter = IncomeExpenseTitleAdapter(requireContext(), it)

        }

        detailsViewModel.fillExpenseSpinner.observe(viewLifecycleOwner) {
            binding.expenseSpinner.adapter = IncomeExpenseTitleAdapter(requireContext(), it)

        }


        binding.startDate.setOnClickListener {

            val minDate: Calendar = Calendar.getInstance()
            val maxDate: Calendar = Calendar.getInstance()
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 10)
            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 10)


            val currentCalendar = Calendar.getInstance()
//            currentCalendar.time = your long
            activity?.let { it1 ->
                object : DatePicker.Builder() {}
                    .id(1)
                    .minDate(minDate)
                    .maxDate(maxDate)
                    .date(currentCalendar)
                    .build { id, calendar, day, month, year ->

                        //binding.dateExpenseEntry.text = "$year / $month / $day"
                        startMillie = calendar?.timeInMillis
                        binding.startDate.text = "$year / $month / $day"
                        Log.i(TAG, "onViewCreated: $startMillie")

                    }
                    .show(it1.supportFragmentManager, "")
            }

        }

        binding.endDate.setOnClickListener {

            val minDate: Calendar = Calendar.getInstance()
            val maxDate: Calendar = Calendar.getInstance()
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 10)
            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 10)


            val currentCalendar = Calendar.getInstance()
//            currentCalendar.time = your long
            activity?.let { it1 ->
                object : DatePicker.Builder() {}
                    .id(1)
                    .minDate(minDate)
                    .maxDate(maxDate)
                    .date(currentCalendar)
                    .build { id, calendar, day, month, year ->

                        //binding.dateExpenseEntry.text = "$year / $month / $day"
                        endMillie = calendar?.timeInMillis
                        binding.endDate.text = "$year / $month / $day"
                        Log.i(TAG, "onViewCreated: $endMillie")

                    }
                    .show(it1.supportFragmentManager, "")
            }

        }

        binding.searchButton.setOnClickListener {
            Log.i(TAG, "onViewCreated: $title")

            detailsViewModel.detailsList.observe(viewLifecycleOwner) {
                detailsAdapter.submitList(it)
                Log.i(TAG, "onViewCreated: $it")
            }


            if (binding.radioGroup.checkedRadioButtonId != -1 && (title != "انتخاب گروه هزینه ای" || title != "انتخاب گروه درآمدی") && startMillie != null && endMillie != null) {
                detailsViewModel.detailsQuery(title!!, startMillie!!, endMillie!!, amountType!!)


            } else {
                if (binding.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        "یکی از دو گزینه هزینه ها ، درآمد ها انتخاب گردد",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (amountType == 0 && title == "انتخاب گروه هزینه ای") {
                    Toast.makeText(
                        requireContext(),
                        "گروه هزینه ای انتخاب گردد",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                if (amountType == 1 && title == "انتخاب گروه هزینه ای") {
                    Toast.makeText(
                        requireContext(),
                        "گروه درآمدی انتخاب گردد",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                if (startMillie == null) {
                    Toast.makeText(requireContext(), "تاریخ شروع درج گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }


                if (endMillie == null) {
                    Toast.makeText(requireContext(), "تاریخ پایان درج گردد", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

            }

        }

    }

}