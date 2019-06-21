package com.example.sayaradzmb.model


data class Annonce(
    var idAnnonce: Int?,
    var Prix: String?,
    var idAutomobiliste: String?,
    var Couleur : String?,
    var Carburant : String?,
    var CodeVersion: Int?,
    var CodeCouleur: Int?,
    var Km: String,
    var Description: String,
    var NombreOffres : Int,
var images: List<CheminImage>?
)