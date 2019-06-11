package com.example.sayaradzmb.model


data class Marque(
    val CodeMarque : Int?,
    val NomMarque : String?,
    val images: List<cheminImage>?

)
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

data class Couleur(
    val CodeCouleur : Int,
    val NomCouleur : String?,
    val CodeHexa : String?,
    val rel_ver_coul : RelationVerCol
)

class RelationVerCol {

}


data class Option (
    val CodeOption :Int?,
    val NomOption : String?,
    val rel_ver_opt : relation
)

data class relation (
    val CodeVersion :Int?
)

data class Tarif(
    val idLigneTarif : Int?,
    val Type : Int?,
    val Code : Int?,
    val DateDebut : String?,
    val DateFin : String?,
    val Prix : Int?
)

