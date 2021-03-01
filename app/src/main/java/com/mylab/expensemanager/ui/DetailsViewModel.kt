package com.mylab.expensemanager.ui

import android.util.Log
import androidx.lifecycle.*
import com.mylab.expensemanager.datamodel.DetailsData
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.chrono.PersianChronologyKhayyam


private const val TAG = "DetailsViewModel"

class DetailsViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    val amountType = MutableLiveData<Int>()


    val fillIncomeSpinner: LiveData<List<ExpenseSpec>> = expenseRepository.fillIncomeSpinner
    val fillExpenseSpinner: LiveData<List<ExpenseSpec>> = expenseRepository.fillExpenseSpinner
    private val readAllExpense: LiveData<List<Expense>> = expenseRepository.readAllExpense
    var detailsList = MediatorLiveData<List<DetailsData>>()



    init {

        detailsList.addSource(readAllExpense) {
            addImageToExpense(it)
        }
    }


    fun incomeChecked() {
        amountType.value = 1
    }

    fun expenseChecked() {
        amountType.value = 0
    }


    fun detailsQuery(
        title: String,
        startDate: Long,
        endDate: Long,
        amountType: Int
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val receivedList = expenseRepository.detailsQuery(title, startDate, endDate, amountType)
            addImageToExpense(receivedList)

        }
    }

    private fun addImageToExpense(it: List<Expense>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<DetailsData>()
            it.forEach { expense ->
                try {
                    val image = expenseRepository.takeImageFromSpec(expense.title)
                    val title = expense.title
                    val amount = expense.amount
                    val date = expense.date
                    val desc = expense.description
                    val amountType = expense.amountType
                    val id = expense.id
                    list.add(DetailsData(id, title, amount, date, image, desc, amountType))

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            withContext(Dispatchers.Main) {
                detailsList.value = list
            }

        }


    }


}