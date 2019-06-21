package com.example.sayaradzmb.model

data class Version(
    val CodeVersion : Int?,
    val CodeModele : Int?,
    val NomVersion : String?,
    val options : List<Option>?,
    val couleurs : List<Couleur>?,
    val images: List<CheminImage>?,
    val lignetarif : Tarif?,
    val modele : VersionExtraInfo,
    val suivie: Boolean

)

