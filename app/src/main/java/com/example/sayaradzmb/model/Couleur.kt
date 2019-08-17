package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Couleur(
    val CodeCouleur: Int,
    val NomCouleur: String?,
    val CodeHexa: String?,
    val CheminImage : String?,
    val rel_ver_coul: RelationVerCol?,
    val tarifCouleur : tarifColor?
) : Parcelable


@Parcelize
data class tarifColor(
    val DateDebut : String,
    val DateFin : String,
    val Prix : Int
) : Parcelable