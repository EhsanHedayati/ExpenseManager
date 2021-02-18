package com.mylab.expensemanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mylab.expensemanager.IncomeListAdapter
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentIncomeBinding
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.IndexOutOfBoundsException

private const val TAG = "IncomeFragment"

class IncomeFragment : Fragment() {
    lateinit var binding: FragmentIncomeBinding
    private val incomeViewModel: IncomeViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.incomeRegisterButton.setOnClickListener {
            findNavController().navigate(
                IncomeFragmentDirections.actionIncomeFragmentToIncomeEntryFragment(1)
            )

        }


        val listAdapter = IncomeListAdapter()
        binding.incomeRecycler.adapter = listAdapter
        weekInfo(listAdapter)
        getWeekChart()
        observeExpense(listAdapter)

        binding.weekButton.setOnClickListener {
            incomeViewModel.dateType.value = 1
            listAdapter.submitList(emptyList())
            weekInfo(listAdapter)
            getWeekChart()
        }

        binding.monthButton.setOnClickListener {
            incomeViewModel.dateType.value = 2
            listAdapter.submitList(emptyList())
            monthInfo(listAdapter)
            getMonthChart()

        }

        binding.yearButton.setOnClickListener {
            incomeViewModel.dateType.value = 3
            listAdapter.submitList(emptyList())
            yearInfo(listAdapter)
            getYearChart()
        }



        binding.incomeModel = incomeViewModel
        binding.lifecycleOwner = this


    }

    private fun getYearChart() {

        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        incomeViewModel.yearChartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            val barDataSet = BarDataSet(barEntry, "")
            val barData = BarData(barDataSet)
            barDataSet.color = R.color.navy_blue
            barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            binding.incomeBarChart.data = barData
            binding.incomeBarChart.animateY(2000)
            val xAxis = binding.incomeBarChart.xAxis
            val yAxisLeft = binding.incomeBarChart.getAxis(YAxis.AxisDependency.LEFT)
            val yAxisRight = binding.incomeBarChart.getAxis(YAxis.AxisDependency.RIGHT)
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxisRight.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            xAxis.valueFormatter = object : IndexAxisValueFormatter(labelsName) {}
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.setDrawAxisLine(false)
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.labelCount = labelsName.size
            xAxis.labelRotationAngle = 270f
            binding.incomeBarChart.invalidate()

        }

    }

    private fun getMonthChart() {
        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        incomeViewModel.monthChartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            val barDataSet = BarDataSet(barEntry, "")
            val barData = BarData(barDataSet)
            barDataSet.color = R.color.navy_blue
            barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            binding.incomeBarChart.data = barData
            binding.incomeBarChart.animateY(2000)
            val xAxis = binding.incomeBarChart.xAxis
            val yAxisLeft = binding.incomeBarChart.getAxis(YAxis.AxisDependency.LEFT)
            val yAxisRight = binding.incomeBarChart.getAxis(YAxis.AxisDependency.RIGHT)
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxisRight.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            xAxis.valueFormatter = object : IndexAxisValueFormatter(labelsName) {}
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.setDrawAxisLine(false)
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.labelCount = labelsName.size
            xAxis.labelRotationAngle = 270f
            binding.incomeBarChart.invalidate()

        }
    }

    private fun getWeekChart() {
        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        incomeViewModel.weekChartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            val barDataSet = BarDataSet(barEntry, "")
            val barData = BarData(barDataSet)
            barDataSet.color = R.color.navy_blue
            barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            binding.incomeBarChart.data = barData
            binding.incomeBarChart.animateY(2000)
            val xAxis = binding.incomeBarChart.xAxis
            val yAxisLeft = binding.incomeBarChart.getAxis(YAxis.AxisDependency.LEFT)
            val yAxisRight = binding.incomeBarChart.getAxis(YAxis.AxisDependency.RIGHT)
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxisRight.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            xAxis.valueFormatter = object : IndexAxisValueFormatter(labelsName) {}
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.setDrawAxisLine(false)
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.labelCount = labelsName.size
            xAxis.labelRotationAngle = 270f
            binding.incomeBarChart.invalidate()

        }

    }

    private fun observeExpense(listAdapter: IncomeListAdapter){
        incomeViewModel.expense.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeExpense: $it")
            try {
                listAdapter.notifyDataSetChanged()
                listAdapter.submitList(incomeViewModel.expense.value?.subList(0, 1))
                listAdapter.submitList(incomeViewModel.expense.value?.subList(0, 2))


            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }


            binding.incomeMoreLessImg.setOnClickListener {

                if (binding.incomeBarChart.visibility == View.VISIBLE) {
                    listAdapter.submitList(incomeViewModel.expense.value)
                    binding.incomeBarChart.visibility = View.GONE
                    binding.incomeMoreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    try {
                        listAdapter.submitList(incomeViewModel.expense.value?.subList(0, 2))
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                    binding.incomeBarChart.visibility = View.VISIBLE
                    binding.incomeMoreLessImg.setImageResource(R.drawable.ic_baseline_expand_more_24)
                }


            }

        }

    }
    private fun yearInfo(listAdapter: IncomeListAdapter) {

        binding.yearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.weekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.monthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.yearButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.weekButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )
        binding.monthButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )

//        incomeViewModel.yearExpenseSpecs.observe(viewLifecycleOwner) {
//
//
//            try {
//                listAdapter.notifyDataSetChanged()
//                listAdapter.submitList(incomeViewModel.yearExpenseSpecs.value?.subList(0, 1))
//                listAdapter.submitList(incomeViewModel.yearExpenseSpecs.value?.subList(0, 2))
//            } catch (e: IndexOutOfBoundsException) {
//                Log.e(TAG, "yearInfo: ", e)
////                e.printStackTrace()
//            }
//
//
//            binding.incomeMoreLessImg.setOnClickListener {
//
//                if (binding.incomeBarChart.visibility == View.VISIBLE) {
//                    listAdapter.submitList(incomeViewModel.yearExpenseSpecs.value)
//                    binding.incomeBarChart.visibility = View.GONE
//                    binding.incomeMoreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
//                } else {
//                    try {
//                        listAdapter.submitList(
//                            incomeViewModel.yearExpenseSpecs.value?.subList(
//                                0,
//                                2
//                            )
//                        )
//                    } catch (e: IndexOutOfBoundsException) {
//                        e.printStackTrace()
//                    }
//                    binding.incomeBarChart.visibility = View.VISIBLE
//                    binding.incomeMoreLessImg.setImageResource(R.drawable.ic_baseline_expand_more_24)
//                }
//
//
//            }
//
//        }

    }

    private fun monthInfo(listAdapter: IncomeListAdapter) {
        binding.monthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.weekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.yearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.monthButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.weekButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )
        binding.yearButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )

//        incomeViewModel.monthExpenseSpecs.observe(viewLifecycleOwner) {
//
//
//            try {
//                listAdapter.notifyDataSetChanged()
//                listAdapter.submitList(incomeViewModel.monthExpenseSpecs.value?.subList(0, 1))
//                listAdapter.submitList(incomeViewModel.monthExpenseSpecs.value?.subList(0, 2))
//            } catch (e: IndexOutOfBoundsException) {
//                e.printStackTrace()
//            }
//
//
//            binding.incomeMoreLessImg.setOnClickListener {
//
//                if (binding.incomeBarChart.visibility == View.VISIBLE) {
//                    listAdapter.submitList(incomeViewModel.monthExpenseSpecs.value)
//                    binding.incomeBarChart.visibility = View.GONE
//                    binding.incomeMoreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
//                } else {
//                    try {
//                        listAdapter.submitList(
//                            incomeViewModel.monthExpenseSpecs.value?.subList(
//                                0,
//                                2
//                            )
//                        )
//                    } catch (e: IndexOutOfBoundsException) {
//                        e.printStackTrace()
//                    }
//                    binding.incomeBarChart.visibility = View.VISIBLE
//                    binding.incomeMoreLessImg.setImageResource(R.drawable.ic_baseline_expand_more_24)
//                }
//
//
//            }
//
//        }


    }

    private fun weekInfo(listAdapter: IncomeListAdapter) {
        binding.weekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.monthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.yearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)

        binding.weekButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.monthButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )
        binding.yearButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )

    }


}