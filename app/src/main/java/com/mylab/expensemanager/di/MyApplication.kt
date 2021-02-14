package com.mylab.expensemanager.di

import android.app.Application
import com.mylab.expensemanager.R
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    private val incomeSpec = mutableListOf<ExpenseSpec>()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(
                this@MyApplication
            )
            modules(
                viewModelModule,
                dataBaseModule

            )
        }




    }


}