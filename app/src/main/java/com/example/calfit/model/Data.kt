package com.example.calfit.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Data(
    val variants: ArrayList<Variant>?
) : Parcelable