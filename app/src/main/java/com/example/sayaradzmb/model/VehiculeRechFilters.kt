package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable

data class VehiculeRechFilters (
    var codeVersion : Int ?,
    var carburant: String?,
    var minPrix : Int?,
    var maxPrix : Int?,
    var minAnnee : Int?,
    var maxAnnee : Int?,
    var maxKm : Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(Float::class.java.classLoader) as? Int,
        parcel.readValue(Float::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(codeVersion)
        parcel.writeValue(carburant)
        parcel.writeValue(minPrix)
        parcel.writeValue(maxPrix)
        parcel.writeValue(minAnnee)
        parcel.writeValue(maxAnnee)
        parcel.writeValue(maxKm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VehiculeRechFilters> {
        override fun createFromParcel(parcel: Parcel): VehiculeRechFilters {
            return VehiculeRechFilters(parcel)
        }

        override fun newArray(size: Int): Array<VehiculeRechFilters?> {
            return arrayOfNulls(size)
        }
    }
}