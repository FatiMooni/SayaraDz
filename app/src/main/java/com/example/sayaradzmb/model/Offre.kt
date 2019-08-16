package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Offre(
    var idOffre: Int,
    var idAutomobiliste: String,
    var Montant : String,
    var idAnnonce: String,
    var Date : String,
    var Etat : Int,
    var automobiliste : Automobiliste,
    var vehicule : VersionInfo,
    var annonce : AnnonceInfo

): Comparable<Offre> {
    override fun compareTo(other: Offre): Int {
        return 0
    }


}

data class AnnonceInfo(
    var idAnnonce: Int,
    var Prix: String
)