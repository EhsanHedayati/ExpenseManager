package com.mylab.expensemanager.ui

import android.util.Log
import androidx.lifecycle.*
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.Duration
import com.mylab.expensemanager.datamodel.ChartInfo
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import com.mylab.expensemanager.util.firsDayOfMonth
import com.mylab.expensemanager.util.firsDayOfWeek
import com.mylab.expensemanager.util.firsDayOfYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
    private val TAG = "ExpenseViewModel"


    private val persianCalendar = PersianCalendar()
    private val day = persianCalendar.persianDay
    private val month = persianCalendar.persianMonthName
    val dayMonth = "$day  $month"

    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense
    val balance = MediatorLiveData<Long>()


    val expenseData = MediatorLiveData<MutableList<ExpenseSpec>>()
    val chartData = MediatorLiveData<MutableList<ChartInfo>>()
    val dateType = MutableLiveData(Duration.WEEK.value)
    val isListEmpty = MediatorLiveData<Boolean>()
    val weekExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val monthExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val yearExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val weekChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val monthChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val yearChartData = MediatorLiveData<MutableList<ChartInfo>>()

    init {
        balance.addSource(onLineIncome, this::onChanged)
        balance.addSource(onLineExpense, this::onChanged)




        chartData.addSource(expenseRepository.readAllExpenseSpec){
            when (dateType.value) {
                Duration.WEEK.value -> getWeekChartInfo(it)
                Duration.MONTH.value -> getMonthChartInfo(it)
                Duration.YEAR.value -> getYearChartInfo(it)
            }

        }

        chartData.addSource(expenseRepository.totalLiveExpense){
            expenseRepository.readAllExpenseSpec.value?.let {
                when (dateType.value) {
                    Duration.WEEK.value -> getWeekChartInfo(it)
                    Duration.MONTH.value -> getMonthChartInfo(it)
                    Duration.YEAR.value -> getYearChartInfo(it)
                }

            }
        }

        chartData.addSource(dateType){
            expenseRepository.readAllExpenseSpec.value?.let {
                when (dateType.value) {
                    Duration.WEEK.value -> getWeekChartInfo(it)
                    Duration.MONTH.value -> getMonthChartInfo(it)
                    Duration.YEAR.value -> getYearChartInfo(it)
                }

            }
        }

        expenseData.addSource(expenseRepository.readAllExpenseSpec){
            when(dateType.value){
                Duration.WEEK.value -> addWeekDataToSpec(it)
                Duration.MONTH.value -> addMonthDataToSpec(it)
                Duration.YEAR.value -> addYearDataToSpec(it)
            }

        }

        expenseData.addSource(expenseRepository.totalLiveExpense){
            expenseRepository.readAllExpenseSpec.value?.let {
                when(dateType.value){
                    Duration.WEEK.value -> addWeekDataToSpec(it)
                    Duration.MONTH.value -> addMonthDataToSpec(it)
                    Duration.YEAR.value -> addYearDataToSpec(it)
                }

            }
        }
        expenseData.addSource(dateType){
            expenseRepository.readAllExpenseSpec.value?.let {
                when(dateType.value){
                    Duration.WEEK.value -> addWeekDataToSpec(it)
                    Duration.MONTH.value -> addMonthDataToSpec(it)
                    Duration.YEAR.value -> addYearDataToSpec(it)
                }

            }
        }

        isListEmpty.addSource(expenseData){
            isListEmpty.value = it.isNullOrEmpty()
        }



       /* weekExpenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addWeekDataToSpec(it)
        }

        weekExpenseSpecs.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                addWeekDataToSpec(it)
            }
        }

        monthExpenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addMonthDataToSpec(it)
        }

        monthExpenseSpecs.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                addMonthDataToSpec(it)
            }
        }

        yearExpenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addYearDataToSpec(it)
        }

        yearExpenseSpecs.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                addYearDataToSpec(it)
            }
        }*/

       /* weekChartData.addSource(expenseRepository.readAllExpenseSpec) {
            getWeekChartInfo(it)
        }
        weekChartData.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                getWeekChartInfo(it)
            }
        }

        monthChartData.addSource(expenseRepository.readAllExpenseSpec) {
            getMonthChartInfo(it)
        }
        monthChartData.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                getMonthChartInfo(it)
            }
        }

        yearChartData.addSource(expenseRepository.readAllExpenseSpec) {
            getYearChartInfo(it)
        }
        yearChartData.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                getYearChartInfo(it)
            }
        }*/

    }

    private fun getYearChartInfo(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ChartInfo>()
            it.forEach { expenseSpec ->
                try {
                    val value = yearParametric(expenseSpec.title)
                    if (value > 0) {
                        val label = expenseSpec.title
                        val chartInfo = ChartInfo(value, label)
                        list.add(chartInfo)
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            chartData.postValue(list)
        }


    }

    private fun getMonthChartInfo(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ChartInfo>()
            it.forEach { expenseSpec ->
                try {
                    val value = monthParametric(expenseSpec.title)
                    if (value > 0) {
                        val label = expenseSpec.title
                        val chartInfo = ChartInfo(value, label)
                        list.add(chartInfo)
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            chartData.postValue(list)
        }

    }

    private fun getWeekChartInfo(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ChartInfo>()
            it.forEach { expenseSpec ->
                try {
                    val value = weekParametric(expenseSpec.title)
                    if (value > 0) {
                        val label = expenseSpec.title
                        val chartInfo = ChartInfo(value, label)
                        list.add(chartInfo)
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            chartData.postValue(list)
        }

    }

    private fun addWeekDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val expense = weekExpense()
            if (expense != null) {
                it.forEach { expenseSpec ->

                    try {
                        val value = weekParametric(expenseSpec.title)
                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / expense
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }

            expenseData.postValue(list)

        }

    }


    private fun addMonthDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val expense = monthExpense()
            if (expense != null) {
                it.forEach { expenseSpec ->

                    try {

                        val value = monthParametric(expenseSpec.title)

                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / expense
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            expenseData.postValue(list)

        }


    }

    private fun addYearDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val expense = yearExpense()
            if (expense != null) {
                it.forEach { expenseSpec ->

                    try {

                        val value = yearParametric(expenseSpec.title)

                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / expense
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            expenseData.postValue(list)

        }

    }


    /*private fun oneYearAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        return cal.timeInMillis
    }*/

    private suspend fun yearExpense(
        startDate: Long = firsDayOfYear() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time

    ): Long? {
        return expenseRepository.weekExpense(startDate, endDate)
    }

    private suspend fun yearParametric(
        title: String,
        startDate: Long = firsDayOfYear() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekExpenseParametric(title, startDate, endDate)
    }



    private suspend fun monthExpense(
        startDate: Long = firsDayOfMonth() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekExpense(startDate, endDate)
    }

    private suspend fun monthParametric(
        title: String,
        startDate: Long = firsDayOfMonth() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekExpenseParametric(title, startDate, endDate)
    }



    private suspend fun weekExpense(
        startDate: Long = firsDayOfWeek() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekExpense(startDate, endDate)
    }

    private suspend fun weekParametric(
        title: String,
        startDate: Long = firsDayOfWeek() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long {

        return expenseRepository.weekExpenseParametric(title, startDate, endDate)
    }

    private fun onChanged(ignored: Long?) {
        var income = onLineIncome.value
        var expense = onLineExpense.value

        // correct value or throw exception if appropriate
        if (income == null) {
            income = 0
        }
        if (expense == null) {
            expense = 0
        }

        balance.value = income - expense
    }


}