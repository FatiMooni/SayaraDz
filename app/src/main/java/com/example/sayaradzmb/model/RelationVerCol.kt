package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

class RelationVerCol() : Parcelable {
    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RelationVerCol> {
        override fun createFromParcel(parcel: Parcel): RelationVerCol {
            return RelationVerCol(parcel)
        }

        override fun newArray(size: Int): Array<RelationVerCol?> {
            return arrayOfNulls(size)
        }
    }

}