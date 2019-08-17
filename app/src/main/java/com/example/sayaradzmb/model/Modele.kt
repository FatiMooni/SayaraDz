package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Modele(
    val CodeModele : Int?,
    val CodeMarque : Int?,
    val NomModele : String?,
    val versions : List<Version>?,
    val options : List<Option>?,
    val couleurs : List<Couleur>?,
    val images: List<CheminImage>?,
    val suivie : Boolean
) : Parcelable