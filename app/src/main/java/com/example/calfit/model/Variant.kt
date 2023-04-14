package com.example.calfit.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Variant(
    val name: String?,
    val subcategories: ArrayList<Subcategory>?,
) : Parcelable