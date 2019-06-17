package com.example.sayaradzmb.model

data class Modele(
    val CodeModele : Int?,
    val CodeMarque : Int?,
    val NomModele : String?,
    val versions : List<version>?,
    val options : List<Option>?,
    val couleurs : List<Couleur>?,
    val images: List<cheminImage>?,
    val suivie : Boolean
)