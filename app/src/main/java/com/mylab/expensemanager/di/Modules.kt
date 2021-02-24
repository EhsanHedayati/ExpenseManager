package com.mylab.expensemanager.di

import com.mylab.expensemanager.db.ExpenseRepository
import com.mylab.expensemanager.ui.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { DeleteIncomeExpenseViewModel(get()) }
    viewModel { AddIncomeExpenseViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { EditDeleteViewModel(get()) }
    viewModel { ExpenseEntryViewModel(get()) }
    viewModel { ExpenseViewModel(get()) }
    viewModel { ExtraIncomeExpenseViewModel() }
    viewModel { IncomeEntryViewModel(get()) }
    viewModel { IncomeViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SettingViewModel() }


}

val dataBaseModule = module {

    single { provideDatabase(androidApplication()) }
    single { provideExpenseDao(get()) }
    factory { ExpenseRepository(get()) }
}


