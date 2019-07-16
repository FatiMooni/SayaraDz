package com.example.sayaradzmb.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.repository.servics.AnnonceService
import com.example.sayaradzmb.servics.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class AjouterAnnonceViewModel : ViewModel() {

    //the live data entities
    private var listMarque: MutableLiveData<Map<Int?, String?>>? = null
    private var listModele: MutableLiveData<Map<Int?, String?>>? = null
    private var listVersion: MutableLiveData<Map<Int?, String?>>? = null

    //To return the different entities
    fun getMarque(): LiveData<Map<Int?, String?>> {
        if (listMarque == null) {
            listMarque = MutableLiveData()
        }
        return listMarque!!
    }

    fun getModele(): LiveData<Map<Int?, String?>> {
        if (listModele == null) {
            listModele = MutableLiveData()
        }
        return listModele!!
    }

    fun getVersion(): LiveData<Map<Int?, String?>> {
        if (listVersion == null) {
            listVersion = MutableLiveData()
        }
        return listVersion!!
    }

    //getting the data from the api
    @SuppressLint("UseSparseArrays")
    fun loadMarque() {
        val marqueService = createMarqueService()
        val requestCall = marqueService.getMarques()
        val marqueMap = HashMap<Int?, String?>()

        requestCall.enqueue(object : Callback<List<Marque>> {
            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.i("Get marque", "Failed gi")
            }

            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) {
                if (response.isSuccessful) {

                    val list = response.body()!!
                    //récuperer les données et les mettre dans une liste apart
                    list.forEach {
                        marqueMap[it.CodeMarque] = it.NomMarque
                    }

                    //pass it into the live data list
                    listMarque!!.value = marqueMap

                } else {
                    Log.i("Get marque", " the request passed but without a an answer")
                }

            }
        })

    }

    //les modeles
    @SuppressLint("UseSparseArrays")
    fun loadModele(idUser: BigInteger, codeMarque: Int) {
        var modeleService = createModeleServce()
        val requestCall = modeleService.getModeles(idUser, codeMarque)
        val modeleMap = HashMap<Int?, String?>()

        requestCall.enqueue(object : Callback<List<Modele>> {

            override fun onResponse(call: Call<List<Modele>>, response: Response<List<Modele>>) {
                if (response.isSuccessful) {
                    val list = response.body()!!
                    list.forEach {
                        modeleMap[it.CodeModele] = it.NomModele
                    }
                    listModele!!.value = modeleMap

                } else {
                    Log.i("get modeles", "i got a response but not success")
                }
            }

            override fun onFailure(call: Call<List<Modele>>, t: Throwable) {
                Log.i("get modeles", "Failed girl !!!", t)
            }
        })
    }

    //les versions
    // envoyer la requete de modeles
    @SuppressLint("UseSparseArrays")
    fun loadVersion(idUser: BigInteger, codeModele : Int) {
        val versionService = createversionService()
        val requestCall = versionService.getVersions(idUser, codeModele)
        val versionMap = HashMap<Int?, String?>()

        requestCall.enqueue(object : Callback<List<Version>> {

            override fun onResponse(call: Call<List<Version>>, response: Response<List<Version>>) =
                if (response.isSuccessful) {
                    val list = response.body()!!

                    list.forEach { e ->
                        versionMap[e.CodeVersion] = e.NomVersion
                    }

                    listVersion!!.value = versionMap

                } else {

                }

            override fun onFailure(call: Call<List<Version>>, t: Throwable) {
            }
        })
    }

    //to create the different services
    /*
     Pour la creation des services
    */
    private fun createversionService(): VersionService {
        return ServiceBuilder.buildService(VersionService::class.java)
    }

    private fun createMarqueService(): MarqueService {
        return ServiceBuilder.buildService(MarqueService::class.java)
    }

    private fun createModeleServce(): ModeleService {
        return ServiceBuilder.buildService(ModeleService::class.java)
    }

    private fun createOptionService(): OptionService {
        return ServiceBuilder.buildService(OptionService::class.java)
    }

    private fun createAnnonceService(): AnnonceService {
        return ServiceBuilder.buildService(AnnonceService::class.java)
    }
}