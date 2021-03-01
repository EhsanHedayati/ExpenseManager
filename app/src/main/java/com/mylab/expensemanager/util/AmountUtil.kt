package com.mylab.expensemanager.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

@BindingAdapter("amount")
fun TextView.amount(amount: String?){
    if (amount!=null) {
    this.text = getThousandSeparate(amount)
    }
}

fun getThousandSeparate(amount:String?):String{
    return if (amount!=null) {
        val symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
        symbols.groupingSeparator = ','

        val formatter = DecimalFormat("###,###", symbols)
        formatter.format(amount.toLong())
    }else{
        ""
    }
}

