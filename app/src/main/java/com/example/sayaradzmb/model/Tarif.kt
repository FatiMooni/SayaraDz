package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Tarif(
    val idLigneTarif : Int?,
    val Type : Int?,
    val Code : Int?,
    val DateDebut : String?,
    val DateFin : String?,
    val Prix : Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idLigneTarif)
        parcel.writeValue(Type)
        parcel.writeValue(Code)
        parcel.writeString(DateDebut)
        parcel.writeString(DateFin)
        parcel.writeValue(Prix)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tarif> {
        override fun createFromParcel(parcel: Parcel): Tarif {
            return Tarif(parcel)
        }

        override fun newArray(size: Int): Array<Tarif?> {
            return arrayOfNulls(size)
        }
    }
}

