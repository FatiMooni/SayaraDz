package com.example.sayaradzmb.model

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

)

data class AnnonceInfo(
    var idAnnonce: Int,
    var Prix: String
)