package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mylab.expensemanager.Duration
import com.mylab.expensemanager.IncomeListAdapter
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentExpenseBinding
import org.koin.android.ext.android.inject


class ExpenseFragment : Fragment() {
    lateinit var binding: FragmentExpenseBinding
    private val expenseViewModel: ExpenseViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.expenseRegisterButton.setOnClickListener {
            findNavController().navigate(
                ExpenseFragmentDirections.actionExpenseFragmentToExpenseEntryFragment(0)
            )

        }

        val listAdapter = IncomeListAdapter()
        binding.expenseRecycler.adapter = listAdapter
        weekInfo(listAdapter)
        getWeekChart()
        observeExpense(listAdapter)


        binding.expenseWeekButton.setOnClickListener {
            expenseViewModel.dateType.value = Duration.WEEK.value
            listAdapter.submitList(emptyList())
            weekInfo(listAdapter)
            getWeekChart()

        }

        binding.expenseMonthButton.setOnClickListener {
            expenseViewModel.dateType.value = Duration.MONTH.value
            listAdapter.submitList(emptyList())
            monthInfo(listAdapter)
            getMonthChart()

        }

        binding.expenseYearButton.setOnClickListener {
            expenseViewModel.dateType.value = Duration.YEAR.value
            listAdapter.submitList(emptyList())
            yearInfo(listAdapter)
            getYearChart()

        }

        binding.expenseModel = expenseViewModel
        binding.lifecycleOwner = this


    }


    private fun getYearChart() {

        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        expenseViewModel.chartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            chartSettings(barEntry, labelsName)


        }

    }

    private fun getMonthChart() {
        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()

        expenseViewModel.chartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            chartSettings(barEntry, labelsName)

        }
    }

    private fun getWeekChart() {
        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        expenseViewModel.chartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            chartSettings(barEntry, labelsName)

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
        binding.expenseBarChart.data = barData
        binding.expenseBarChart.description.text = ""

        val xAxis = binding.expenseBarChart.xAxis
        val yAxisLeft = binding.expenseBarChart.getAxis(YAxis.AxisDependency.LEFT)
        val yAxisRight = binding.expenseBarChart.getAxis(YAxis.AxisDependency.RIGHT)
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
        binding.expenseBarChart.animateY(2000)
        binding.expenseBarChart.invalidate()
    }

    private fun yearInfo(listAdapter: IncomeListAdapter) {

        binding.expenseYearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.expenseWeekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseMonthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseYearButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.expenseWeekButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )
        binding.expenseMonthButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )


    }

    private fun observeExpense(listAdapter: IncomeListAdapter) {
        expenseViewModel.expenseData.observe(viewLifecycleOwner) {


            try {
                listAdapter.notifyDataSetChanged()
                listAdapter.submitList(expenseViewModel.expenseData.value?.subList(0, 1))
                listAdapter.submitList(expenseViewModel.expenseData.value?.subList(0, 2))
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }


            binding.moreLessImg.setOnClickListener {

                if (binding.expenseBarChart.visibility == View.VISIBLE) {
                    listAdapter.submitList(expenseViewModel.expenseData.value)
                    binding.expenseBarChart.visibility = View.GONE
                    binding.moreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    try {
                        listAdapter.submitList(
                            expenseViewModel.expenseData.value?.subList(
                                0,
                                2
                            )
                        )
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }
                    binding.expenseBarChart.visibility = View.VISIBLE
                    binding.moreLessImg.setImageResource(R.drawable.ic_baseline_expand_more_24)
                }


            }

        }
    }

    private fun monthInfo(listAdapter: IncomeListAdapter) {
        binding.expenseMonthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.expenseWeekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseYearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseMonthButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.expenseWeekButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )
        binding.expenseYearButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )


    }

    private fun weekInfo(listAdapter: IncomeListAdapter) {

        binding.expenseWeekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.expenseMonthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseYearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)

        binding.expenseWeekButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.expenseMonthButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )
        binding.expenseYearButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.default_button_text
            )
        )

    }


}