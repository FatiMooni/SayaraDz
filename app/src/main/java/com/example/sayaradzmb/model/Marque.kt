package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Marque(
    val CodeMarque : Int?,
    val NomMarque : String?,
    val images: List<CheminImage>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createTypedArrayList(CheminImage)
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(CodeMarque)
        parcel.writeString(NomMarque)
        parcel.writeTypedList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Marque> {
        override fun createFromParcel(parcel: Parcel): Marque {
            return Marque(parcel)
        }

        override fun newArray(size: Int): Array<Marque?> {
            return arrayOfNulls(size)
        }
    }
}
