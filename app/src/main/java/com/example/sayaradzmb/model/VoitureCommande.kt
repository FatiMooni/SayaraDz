package com.example.sayaradzmb.model

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField


data class VoitureCommande (
     var vehicules : ArrayList<TypeVoiture>,
     var Montant : Int,
     var options: List<Option>,
     var quantite : Int,
     var CodeHexa : String

):Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("vehicules"),
        parcel.readInt(),
        parcel.createTypedArrayList(Option),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Montant)
        parcel.writeTypedList(options)
        parcel.writeInt(quantite)
        parcel.writeString(CodeHexa)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VoitureCommande> {
        override fun createFromParcel(parcel: Parcel): VoitureCommande {
            return VoitureCommande(parcel)
        }

        override fun newArray(size: Int): Array<VoitureCommande?> {
            return arrayOfNulls(size)
        }
    }
}

data class TypeVoiture (
    var NumChassis : Int,
    var Concessionaire : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(NumChassis)
        parcel.writeString(Concessionaire)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TypeVoiture> {
        override fun createFromParcel(parcel: Parcel): TypeVoiture {
            return TypeVoiture(parcel)
        }

        override fun newArray(size: Int): Array<TypeVoiture?> {
            return arrayOfNulls(size)
        }
    }
}


