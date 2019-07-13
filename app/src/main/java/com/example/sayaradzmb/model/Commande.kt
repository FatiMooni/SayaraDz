package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Commande (

        var idCommande : Int,
        var Date : String,
        var Montant: Long,
        var Etat: Int,
        var Reservation : Int?,
        var automobiliste : Automobiliste,
        var vehicule : Vehicule
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Automobiliste::class.java.classLoader)!!,
        parcel.readParcelable(Vehicule::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idCommande)
        parcel.writeString(Date)
        parcel.writeLong(Montant)
        parcel.writeInt(Etat)
        parcel.writeValue(Reservation)
        parcel.writeParcelable(automobiliste, flags)
        parcel.writeParcelable(vehicule, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Commande> {
        override fun createFromParcel(parcel: Parcel): Commande {
            return Commande(parcel)
        }

        override fun newArray(size: Int): Array<Commande?> {
            return arrayOfNulls(size)
        }
    }
}