package com.example.sayaradzmb.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sayaradzmb.model.Offre

class UserOffersViewModel : ViewModel() {

    private var offre: MutableLiveData<List<Offre>>? = null


    fun getOffre(): LiveData<List<Offre>> {
        if (offre == null) {
            offre = MutableLiveData()
        }
        return offre!!
    }

    fun loadOffers(id: String) {}

}