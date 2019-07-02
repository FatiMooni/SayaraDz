package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class CheminImage(
    val CheminImage : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(CheminImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheminImage> {
        override fun createFromParcel(parcel: Parcel): CheminImage {
            return CheminImage(parcel)
        }

        override fun newArray(size: Int): Array<CheminImage?> {
            return arrayOfNulls(size)
        }
    }
}