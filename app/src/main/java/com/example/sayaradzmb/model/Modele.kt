package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Modele(
    val CodeModele : Int?,
    val CodeMarque : Int?,
    val NomModele : String?,
    val versions : List<Version>?,
    val options : List<Option>?,
    val couleurs : List<Couleur>?,
    val images: List<CheminImage>?,
    val suivie : Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createTypedArrayList(Version),
        parcel.createTypedArrayList(Option),
        parcel.createTypedArrayList(Couleur),
        parcel.createTypedArrayList(CheminImage),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(CodeModele)
        parcel.writeValue(CodeMarque)
        parcel.writeString(NomModele)
        parcel.writeTypedList(versions)
        parcel.writeTypedList(options)
        parcel.writeTypedList(couleurs)
        parcel.writeTypedList(images)
        parcel.writeByte(if (suivie) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Modele> {
        override fun createFromParcel(parcel: Parcel): Modele {
            return Modele(parcel)
        }

        override fun newArray(size: Int): Array<Modele?> {
            return arrayOfNulls(size)
        }
    }
}
