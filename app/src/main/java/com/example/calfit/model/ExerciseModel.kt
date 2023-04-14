package com.example.calfit.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ExerciseModel(
    val `data`: Data?
) : Parcelable