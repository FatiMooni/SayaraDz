package com.example.sayaradzmb.model



data class Offre(
    var idOffre: Int,
    var idAutomobiliste: String,
    var Montant : String,
    var idAnnonce: String,
    var Date : String,
    var Etat : Int,
    var automobiliste : Automobiliste

): Comparable<Offre> {
    override fun compareTo(other: Offre): Int {
        return if (other.idOffre == this.idOffre) -1
        else idOffre
    }
}

data class UserOffre(
    var idOffre: Int,
    var idAutomobiliste: String,
    var Montant : String,
    var idAnnonce: String,
    var Date : String,
    var Etat : Int,
    var automobiliste : Automobiliste,
    var vehicule : VersionInfo,
    var annonce : AnnonceInfo
) : Comparable<UserOffre>{
    override fun compareTo(other: UserOffre): Int {
        return if (other.idOffre == this.idOffre) -1
        else idOffre
    }

}

data class AnnonceInfo(
    var idAnnonce: Int,
    var Prix: String
)