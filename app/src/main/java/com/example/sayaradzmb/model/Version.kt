package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Version(
    val CodeVersion : Int?,
    val CodeModele : Int?,
    val NomVersion : String?,
    val options : List<Option>?,
    val couleurs : List<Couleur>?,
    val images: List<CheminImage>?,
    val lignetarif : Tarif?,
    val modele : VersionExtraInfo,
    val suivie: Boolean

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createTypedArrayList(Option),
        parcel.createTypedArrayList(Couleur),
        parcel.createTypedArrayList(CheminImage),
        parcel.readParcelable(Tarif::class.java.classLoader),
        parcel.readParcelable(VersionExtraInfo::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(CodeVersion)
        parcel.writeValue(CodeModele)
        parcel.writeString(NomVersion)
        parcel.writeTypedList(options)
        parcel.writeTypedList(couleurs)
        parcel.writeTypedList(images)
        parcel.writeParcelable(lignetarif, flags)
        parcel.writeParcelable(modele, flags)
        parcel.writeByte(if (suivie) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Version> {
        override fun createFromParcel(parcel: Parcel): Version {
            return Version(parcel)
        }

        override fun newArray(size: Int): Array<Version?> {
            return arrayOfNulls(size)
        }
    }
}

