package com.mylab.expensemanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec


@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expense_spec_table WHERE title = :title")
    suspend fun existenceExpenseSpec(title: String): ExpenseSpec

    @Query("DELETE FROM expense_spec_table WHERE title = :title")
    suspend fun deleteExpenseSpec(title: String)

    @Query("DELETE FROM expense_table WHERE title = :title")
    suspend fun deleteExpense(title: String)

    @Insert
    suspend fun insertExpenseSpec(expenseSpec: ExpenseSpec)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun readAllExpense(): LiveData<List<Expense>>

    @Query("DELETE FROM expense_table")
    suspend fun deleteAllExpense()


    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 0")
    suspend fun expenseDateRangeQuery(startDate: Long, endDate: Long): List<Expense>

    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun incomeDateRangeQuery(startDate: Long, endDate: Long): List<Expense>

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun incomeSumQuery(startDate: Long, endDate: Long): Long

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 0")
    suspend fun expenseSumQuery(startDate: Long, endDate: Long): Long

    @Query("SELECT SUM(amount) FROM expense_table WHERE amountType = 1")
    fun totalLiveIncome(): LiveData<Long>

    @Query("SELECT SUM(amount) FROM expense_table WHERE amountType = 0")
    fun totalLiveExpense(): LiveData<Long>

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun weekIncome(startDate: Long, endDate: Long): Long?

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 0")
    suspend fun weekExpense(startDate: Long, endDate: Long): Long?

    @Query("SELECT SUM(amount) FROM expense_table WHERE title = 'حقوق' AND date BETWEEN :startDate AND :endDate")
    fun weekPay(startDate: Long, endDate: Long): LiveData<Long>

    @Query("SELECT * FROM expense_spec_table")
    fun expenseSpecCount(): LiveData<List<ExpenseSpec>>

    @Query("SELECT * FROM expense_spec_table WHERE expenseType = 1")
    fun readAllIncomeSpec(): LiveData<List<ExpenseSpec>>

    @Query("SELECT * FROM expense_spec_table WHERE expenseType = 0")
    fun readAllExpenseSpec(): LiveData<List<ExpenseSpec>>


    @Query("SELECT * FROM expense_spec_table WHERE expenseType = 1")
    fun fillIncomeSpinner(): LiveData<List<ExpenseSpec>>

    @Query("SELECT * FROM expense_spec_table WHERE expenseType = 0")
    fun fillExpenseSpinner(): LiveData<List<ExpenseSpec>>

    @Insert
    @JvmSuppressWildcards
    suspend fun insertAllExpenseSpec(objects: List<ExpenseSpec>)

    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    fun weekIncomeList(startDate: Long, endDate: Long): LiveData<List<Expense>>

    @Query("SELECT title FROM expense_spec_table WHERE expenseType = 1")
    fun incomeTitleList(): LiveData<List<String>>

    @Query("SELECT SUM(amount) FROM expense_table WHERE title = :title AND date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun weekParametric(title: String, startDate: Long, endDate: Long): Long

    @Query("SELECT SUM(amount) FROM expense_table WHERE title = :title AND date BETWEEN :startDate AND :endDate AND amountType = 0")
    suspend fun weekExpenseParametric(title: String, startDate: Long, endDate: Long): Long

    @Query("SELECT title FROM expense_table WHERE date BETWEEN :startDate AND :endDate")
    suspend fun provideTitle(startDate: Long, endDate: Long): List<String>

    @Query("SELECT * FROM expense_table WHERE title = :title AND date BETWEEN :startDate AND :endDate AND amountType = :amountType")
    suspend fun detailsQuery(
        title: String,
        startDate: Long,
        endDate: Long,
        amountType: Int
    ): List<Expense>

    @Query("SELECT image FROM expense_spec_table WHERE title = :title")
    suspend fun takeImageFromSpec(title: String): Int


}