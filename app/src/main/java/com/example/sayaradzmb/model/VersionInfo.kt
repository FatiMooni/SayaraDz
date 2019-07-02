package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class VersionInfo(
    var CodeVersion : Int,
    var NomVersion : String,
    var NomModele : String,
    var NomMarque : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(CodeVersion)
        parcel.writeString(NomVersion)
        parcel.writeString(NomModele)
        parcel.writeString(NomMarque)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VersionInfo> {
        override fun createFromParcel(parcel: Parcel): VersionInfo {
            return VersionInfo(parcel)
        }

        override fun newArray(size: Int): Array<VersionInfo?> {
            return arrayOfNulls(size)
        }
    }
}