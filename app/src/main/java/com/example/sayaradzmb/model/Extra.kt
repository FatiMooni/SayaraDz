package com.example.sayaradzmb.model


data class cheminImage(
    val CheminImage : String?
)

data class  versionExtraInfo (
    val NomModele : String?,
    val marque : Marque
)

data class versionInfo(
     var CodeVersion : Int,
     var NomVersion : String,
     var NomModele : String,
     var NomMarque : String
)