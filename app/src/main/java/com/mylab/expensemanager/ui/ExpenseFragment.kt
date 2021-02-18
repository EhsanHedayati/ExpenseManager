package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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


        binding.expenseWeekButton.setOnClickListener {
            listAdapter.submitList(emptyList())
            weekInfo(listAdapter)
            getWeekChart()

        }

        binding.expenseMonthButton.setOnClickListener {
            listAdapter.submitList(emptyList())
            monthInfo(listAdapter)
            getMonthChart()

        }

        binding.expenseYearButton.setOnClickListener {
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
        expenseViewModel.yearChartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            val barDataSet = BarDataSet(barEntry, "")
            val barData = BarData(barDataSet)
            barDataSet.color = R.color.navy_blue
            barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            binding.expenseBarChart.data = barData
            binding.expenseBarChart.animateY(2000)
            val xAxis = binding.expenseBarChart.xAxis
            val yAxisLeft = binding.expenseBarChart.getAxis(YAxis.AxisDependency.LEFT)
            val yAxisRight = binding.expenseBarChart.getAxis(YAxis.AxisDependency.RIGHT)
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
            binding.expenseBarChart.invalidate()

        }

    }

    private fun getMonthChart() {
        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        expenseViewModel.monthChartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            val barDataSet = BarDataSet(barEntry, "")
            val barData = BarData(barDataSet)
            barDataSet.color = R.color.navy_blue
            barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            binding.expenseBarChart.data = barData
            binding.expenseBarChart.animateY(2000)
            val xAxis = binding.expenseBarChart.xAxis
            val yAxisLeft = binding.expenseBarChart.getAxis(YAxis.AxisDependency.LEFT)
            val yAxisRight = binding.expenseBarChart.getAxis(YAxis.AxisDependency.RIGHT)
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
            binding.expenseBarChart.invalidate()

        }
    }

    private fun getWeekChart() {
        val barEntry = ArrayList<BarEntry>()
        val labelsName = ArrayList<String>()
        expenseViewModel.weekChartData.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, chartInfo ->
                barEntry.add(BarEntry(index.toFloat(), chartInfo.value.toFloat()))
                labelsName.add(chartInfo.label)
            }
            val barDataSet = BarDataSet(barEntry, "")
            val barData = BarData(barDataSet)
            barDataSet.color = R.color.navy_blue
            barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            binding.expenseBarChart.data = barData
            binding.expenseBarChart.animateY(2000)
            val xAxis = binding.expenseBarChart.xAxis
            val yAxisLeft = binding.expenseBarChart.getAxis(YAxis.AxisDependency.LEFT)
            val yAxisRight = binding.expenseBarChart.getAxis(YAxis.AxisDependency.RIGHT)
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
            binding.expenseBarChart.invalidate()

        }

    }

    private fun yearInfo(listAdapter: IncomeListAdapter) {

        binding.expenseYearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.expenseWeekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseMonthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseYearButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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

        expenseViewModel.yearExpenseSpecs.observe(viewLifecycleOwner) {


            try {
                listAdapter.notifyDataSetChanged()
                listAdapter.submitList(expenseViewModel.yearExpenseSpecs.value?.subList(0, 1))
                listAdapter.submitList(expenseViewModel.yearExpenseSpecs.value?.subList(0, 2))
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }


            binding.moreLessImg.setOnClickListener {

                if (binding.expenseBarChart.visibility == View.VISIBLE) {
                    listAdapter.submitList(expenseViewModel.yearExpenseSpecs.value)
                    binding.expenseBarChart.visibility = View.GONE
                    binding.moreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    try {
                        listAdapter.submitList(
                            expenseViewModel.yearExpenseSpecs.value?.subList(
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
        binding.expenseMonthButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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

        expenseViewModel.monthExpenseSpecs.observe(viewLifecycleOwner) {


            try {
                listAdapter.notifyDataSetChanged()
                listAdapter.submitList(expenseViewModel.monthExpenseSpecs.value?.subList(0, 1))
                listAdapter.submitList(expenseViewModel.monthExpenseSpecs.value?.subList(0, 2))
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }


            binding.moreLessImg.setOnClickListener {

                if (binding.expenseBarChart.visibility == View.VISIBLE) {
                    listAdapter.submitList(expenseViewModel.monthExpenseSpecs.value)
                    binding.expenseBarChart.visibility = View.GONE
                    binding.moreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    try {
                        listAdapter.submitList(
                            expenseViewModel.monthExpenseSpecs.value?.subList(
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

    private fun weekInfo(listAdapter: IncomeListAdapter) {

        binding.expenseWeekButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_clicked_shape)
        binding.expenseMonthButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)
        binding.expenseYearButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_shape)

        binding.expenseWeekButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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

        expenseViewModel.weekExpenseSpecs.observe(viewLifecycleOwner) {

            try {
                listAdapter.notifyDataSetChanged()
                listAdapter.submitList(expenseViewModel.weekExpenseSpecs.value?.subList(0, 1))
                listAdapter.submitList(expenseViewModel.weekExpenseSpecs.value?.subList(0, 2))


            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }


            binding.moreLessImg.setOnClickListener {

                if (binding.expenseBarChart.visibility == View.VISIBLE) {
                    listAdapter.submitList(expenseViewModel.weekExpenseSpecs.value)
                    binding.expenseBarChart.visibility = View.GONE
                    binding.moreLessImg.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    try {
                        listAdapter.submitList(expenseViewModel.weekExpenseSpecs.value?.subList(0, 2))
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                    binding.expenseBarChart.visibility = View.VISIBLE
                    binding.moreLessImg.setImageResource(R.drawable.ic_baseline_expand_more_24)
                }


            }

        }

    }


}