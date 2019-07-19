package com.example.sayaradzmb.model

data class VoitureCommande (
    private var couleur : Couleur,
    private var option: List<Option>,
    private var quantite : Int,
    private var prix : Int
)