package com.mylab.expensemanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.mylab.expensemanager.Duration
import com.mylab.expensemanager.IncomeListAdapter
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentIncomeBinding
import org.koin.android.ext.android.inject


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

        binding.incomeModel = incomeViewModel
        binding.lifecycleOwner = this

        binding.incomeRegisterButton.setOnClickListener {

            findNavController().navigate(
                IncomeFragmentDirections.actionIncomeFragmentToIncomeEntryFragment(1)
            )
        }

        val listAdapter = IncomeListAdapter()
        binding.incomeRecycler.adapter = listAdapter


        weekInfo()
        getWeekChart()
        observeExpense(listAdapter)

        binding.weekButton.setOnClickListener {

            incomeViewModel.dateType.value = Duration.WEEK.value
            listAdapter.submitList(emptyList())
            weekInfo()
            getWeekChart()

        }

        binding.monthButton.setOnClickListener {

            incomeViewModel.dateType.value = Duration.MONTH.value
            listAdapter.submitList(emptyList())
            monthInfo()
            getMonthChart()

        }

        binding.yearButton.setOnClickListener {

            incomeViewModel.dateType.value = Duration.YEAR.value
            listAdapter.submitList(emptyList())
            yearInfo()
            getYearChart()


        }

    }

    private fun getYearChart() {

        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        incomeViewModel.chartData.observe(viewLifecycleOwner) {
            if (!incomeViewModel.isChartDataObserved && incomeViewModel.dateType.value == Duration.YEAR.value) {
                it.forEachIndexed { index, chartInfo ->
                    barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                    labelsName.add(chartInfo.label)
                }


                chartSettings(barEntry, labelsName)
            }

        }

    }

    private fun getMonthChart() {

        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
//        chartSettings(barEntry, labelsName)
//
//        binding.incomeBarChart.clear()
//        binding.incomeBarChart.invalidate()
//        binding.incomeBarChart.notifyDataSetChanged()

        incomeViewModel.chartData.observe(viewLifecycleOwner) {
            Log.i(TAG, "getMonthChartMonth: $it")

            if (!incomeViewModel.isChartDataObserved && incomeViewModel.dateType.value == Duration.MONTH.value) {

                it.forEachIndexed { index, chartInfo ->
                    barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                    labelsName.add(chartInfo.label)
                    Log.i(TAG, "getMonthChartMonth: $it")
                }


                chartSettings(barEntry, labelsName)
            }

        }
    }

    private fun getWeekChart() {

        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()

        incomeViewModel.chartData.observe(viewLifecycleOwner) {
            if (!incomeViewModel.isChartDataObserved &&
                incomeViewModel.dateType.value == Duration.WEEK.value) {
                Log.i(TAG, "getWeekChart223: $it")
                it.forEachIndexed { index, chartInfo ->
                    barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                    labelsName.add(chartInfo.label)
                    Log.i(TAG, "getWeekChart22: $it")
                }

                chartSettings(barEntry, labelsName)
                incomeViewModel.isChartDataObserved = true
            }

        }

    }


    private fun chartSettings(
        barEntry: ArrayList<BarEntry>,
        labelsName: ArrayList<String>
    ) {

        val tf = ResourcesCompat.getFont(requireContext(), R.font.vazir_medium)
        val barDataSet = BarDataSet(barEntry, "")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.bar_color)
        barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        barDataSet.valueTextSize = 9f

        val barData = BarData(barDataSet)
        barData.setValueTypeface(tf)
        binding.incomeBarChart.data = barData

        binding.incomeBarChart.description.text = ""
        val xAxis = binding.incomeBarChart.xAxis
        val yAxisLeft = binding.incomeBarChart.getAxis(YAxis.AxisDependency.LEFT)
        val yAxisRight = binding.incomeBarChart.getAxis(YAxis.AxisDependency.RIGHT)
        yAxisRight.isEnabled = false
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        yAxisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        yAxisLeft.typeface = tf
        yAxisLeft.textSize = 12f
        xAxis.typeface = tf
        xAxis.textSize = 12f
        xAxis.valueFormatter = object : IndexAxisValueFormatter(labelsName) {}
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        yAxisLeft.setDrawGridLines(false)
        xAxis.labelCount = labelsName.size
        xAxis.labelRotationAngle = 270f
        binding.incomeBarChart.animateY(2000)
        binding.incomeBarChart.invalidate()
    }

    private fun observeExpense(listAdapter: IncomeListAdapter) {
        incomeViewModel.expense.observe(viewLifecycleOwner) {
            //Log.i(TAG, "observeExpense: $it")
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

    private fun yearInfo() {

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


    }

    private fun monthInfo() {
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


    }

    private fun weekInfo() {
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