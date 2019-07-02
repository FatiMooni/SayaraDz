package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Relation (
    val CodeVersion :Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(CodeVersion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Relation> {
        override fun createFromParcel(parcel: Parcel): Relation {
            return Relation(parcel)
        }

        override fun newArray(size: Int): Array<Relation?> {
            return arrayOfNulls(size)
        }
    }
}