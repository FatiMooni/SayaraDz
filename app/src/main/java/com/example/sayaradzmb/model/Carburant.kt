package com.example.sayaradzmb.model

import com.example.sayaradzmb.R

data class Carburant (val image : Int , val title : String)

//Liste des carburants

val fuel_type : List<Carburant> = listOf(
    Carburant(R.drawable.no_spec , "Not Specified" ) ,
    Carburant(R.drawable.fuel_en , "Essence\nNormale" ) ,
    Carburant(R.drawable.fuel_es ,"Essence\n  Super" ) ,
    Carburant(R.drawable.fuel_sp , "Sans plomb" ),
    Carburant(R.drawable.fuel_oil ,"Gas oil" ),
    Carburant(R.drawable.fuel_gpl ,"GPL/C")
)