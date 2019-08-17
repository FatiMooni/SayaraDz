package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoitureCommande (
     var vehicules : ArrayList<TypeVoiture>,
     var Montant : Int,
     var options: List<Option>,
     var quantite : Int,
     var Couleur : Couleur?,
     var tarifBase : TarifBase?
):Parcelable


@Parcelize
data class TypeVoiture (
    var NumChassis : Int,
    var Concessionaire : String
) : Parcelable

@Parcelize
data class TarifBase (
    val DateDebut : String,
    val DateFin : String,
    val Prix : Int
) : Parcelable