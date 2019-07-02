package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Automobiliste(
    var idAutomobiliste : String?,
    var Nom : String?,
    var Prenom : String?,
    val NumTel: String?
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idAutomobiliste)
        parcel.writeString(Nom)
        parcel.writeString(Prenom)
        parcel.writeString(NumTel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Automobiliste> {
        override fun createFromParcel(parcel: Parcel): Automobiliste {
            return Automobiliste(parcel)
        }

        override fun newArray(size: Int): Array<Automobiliste?> {
            return arrayOfNulls(size)
        }
    }
}