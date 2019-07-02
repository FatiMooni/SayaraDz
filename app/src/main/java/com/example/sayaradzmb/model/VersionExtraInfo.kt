package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class  VersionExtraInfo (
    val NomModele : String?,
    val marque : Marque
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Marque::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(NomModele)
        parcel.writeParcelable(marque, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VersionExtraInfo> {
        override fun createFromParcel(parcel: Parcel): VersionExtraInfo {
            return VersionExtraInfo(parcel)
        }

        override fun newArray(size: Int): Array<VersionExtraInfo?> {
            return arrayOfNulls(size)
        }
    }
}