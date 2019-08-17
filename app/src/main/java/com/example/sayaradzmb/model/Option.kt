package com.example.sayaradzmb.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Option (
    val CodeOption :Int?,
    val NomOption : String?,
    val rel_ver_opt : Relation?,
    val tarifOption : tarifOption?
) : Parcelable



@Parcelize
data class tarifOption(
    val DateDebut : String,
    val DateFin : String,
    val Prix : Int
) : Parcelable