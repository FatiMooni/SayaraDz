package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable


data class Annonce(
    var idAnnonce: Int?,
    var Prix: String?,
    var idAutomobiliste: String?,
    var Couleur : String?,
    var Carburant : String?,
    var version : VersionInfo?,
    var CodeCouleur: Int?,
    var Km: String,
    var Description: String,
    var NombreOffres : Int,
var images: List<CheminImage>?
) : Parcelable ,Comparable<Annonce>{
    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: Annonce): Int {
        return 0
    }

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(VersionInfo::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.createTypedArrayList(CheminImage)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idAnnonce)
        parcel.writeString(Prix)
        parcel.writeString(idAutomobiliste)
        parcel.writeString(Couleur)
        parcel.writeString(Carburant)
        parcel.writeParcelable(version, flags)
        parcel.writeValue(CodeCouleur)
        parcel.writeString(Km)
        parcel.writeString(Description)
        parcel.writeInt(NombreOffres)
        parcel.writeTypedList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Annonce> {
        override fun createFromParcel(parcel: Parcel): Annonce {
            return Annonce(parcel)
        }

        override fun newArray(size: Int): Array<Annonce?> {
            return arrayOfNulls(size)
        }
    }
}