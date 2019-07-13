package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Vehicule (

    var NumChassis : Int,
    var Concessionaire : String,
    var NomMarque: String,
    var NomModele : String,
    var NomVersion : String

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(NumChassis)
        parcel.writeString(Concessionaire)
        parcel.writeString(NomMarque)
        parcel.writeString(NomModele)
        parcel.writeString(NomVersion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vehicule> {
        override fun createFromParcel(parcel: Parcel): Vehicule {
            return Vehicule(parcel)
        }

        override fun newArray(size: Int): Array<Vehicule?> {
            return arrayOfNulls(size)
        }
    }
}