package com.mylab.expensemanager.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailsData(
    val id: Int,
    val title: String,
    val amount: Long,
    val date: Long,
    val Image: Int,
    val desc: String,
    val amountType: Int
):Parcelable