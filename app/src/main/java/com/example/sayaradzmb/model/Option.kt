package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class Option (
    val CodeOption :Int?,
    val NomOption : String?,
    val rel_ver_opt : Relation
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(Relation::class.java.classLoader)!!
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(CodeOption)
        parcel.writeString(NomOption)
        parcel.writeParcelable(rel_ver_opt, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Option> {
        override fun createFromParcel(parcel: Parcel): Option {
            return Option(parcel)
        }

        override fun newArray(size: Int): Array<Option?> {
            return arrayOfNulls(size)
        }
    }
}
