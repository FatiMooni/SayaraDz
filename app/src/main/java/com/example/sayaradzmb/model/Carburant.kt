package com.example.sayaradzmb.model

import com.example.sayaradzmb.R

data class Carburant (val image : Int , val title : String)

//Liste des carburants

val fuel_type : List<Carburant> = listOf(
    Carburant(R.drawable.no_spec , "Not Specified\n" ) ,
    Carburant(R.drawable.fuel_en , "Essence\nNormale\n" ) ,
    Carburant(R.drawable.fuel_es ,"Essence\n  Super\n" ) ,
    Carburant(R.drawable.fuel_sp , "Sans plomb\n" ),
    Carburant(R.drawable.fuel_oil ,"Gas oil\n" ),
    Carburant(R.drawable.fuel_gpl ,"GPL/C")
)