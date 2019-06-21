package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable


data class VehiculeOccasion(

    var idAnnonce : Int,
    var Prix : String,
    var automobiliste : Automobiliste,
    var version : VersionInfo,
    var Couleur: String,
    var Km : String,
    var Carburant: String,
    var Description : String,
    var NombreOffres: String,
    var annnee : String,
    var images: List<CheminImage>?

)
