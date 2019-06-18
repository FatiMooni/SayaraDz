package com.example.sayaradzmb.model


data class VehiculeOccasion(

    var idAnnonce : Int ,
    var Prix : String,
    var automobiliste : Automobiliste,
    var version : versionInfo ,
    var Couleur: String,
    var Km : String,
    var Carburant: String ,
    var Description : String,
    var NombreOffres: String ,
    var annnee : String,
    var images: List<cheminImage>?

)