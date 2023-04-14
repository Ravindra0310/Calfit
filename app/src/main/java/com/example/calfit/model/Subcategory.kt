package com.example.calfit.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Subcategory(
    val title: String?,
    val description: String?,
    val images: String?,
    val youtube_link: String?,
    val duration: Int?,
    var isCompleted:Boolean?=false
) : Parcelable