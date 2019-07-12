package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable


data class VehiculeOccasion(

    var idAnnonce : Int,
    var Prix : String?,
    var automobiliste : Automobiliste?,
    var version : VersionInfo?,
    var Couleur: String?,
    var Km : String?,
    var Carburant: String?,
    var Description : String?,
    var NombreOffres: String?,
    var Annee : String?,
    var images: List<CheminImage>?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString(),
        parcel.readParcelable(Automobiliste::class.java.classLoader),
        parcel.readParcelable(VersionInfo::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(CheminImage)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idAnnonce)
        parcel.writeString(Prix)
        parcel.writeParcelable(automobiliste, flags)
        parcel.writeParcelable(version, flags)
        parcel.writeString(Couleur)
        parcel.writeString(Km)
        parcel.writeString(Carburant)
        parcel.writeString(Description)
        parcel.writeString(NombreOffres)
        parcel.writeString(Annee)
        parcel.writeTypedList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VehiculeOccasion> {
        override fun createFromParcel(parcel: Parcel): VehiculeOccasion {
            return VehiculeOccasion(parcel)
        }

        override fun newArray(size: Int): Array<VehiculeOccasion?> {
            return arrayOfNulls(size)
        }
    }
}
