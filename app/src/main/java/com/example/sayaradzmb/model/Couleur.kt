package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Couleur(
    val CodeCouleur: Int,
    val NomCouleur: String?,
    val CodeHexa: String?,
    val rel_ver_coul: RelationVerCol?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(RelationVerCol::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(CodeCouleur)
        parcel.writeString(NomCouleur)
        parcel.writeString(CodeHexa)
        parcel.writeParcelable(rel_ver_coul, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Couleur> {
        override fun createFromParcel(parcel: Parcel): Couleur {
            return Couleur(parcel)
        }

        override fun newArray(size: Int): Array<Couleur?> {
            return arrayOfNulls(size)
        }
    }
}
