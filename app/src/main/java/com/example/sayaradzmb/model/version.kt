package com.example.sayaradzmb.model

data class version(
    val CodeVersion : Int?,
    val CodeModele : Int?,
    val NomVersion : String?,
    val options : List<Option>?,
    val couleurs : List<Couleur>?,
    val images: List<cheminImage>?,
    val lignetarif : Tarif?,
    val modele : versionExtraInfo,
    val suivie: Boolean

)