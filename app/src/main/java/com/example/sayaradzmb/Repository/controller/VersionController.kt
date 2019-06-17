package com.example.sayaradzmb.Repository.controller

import android.content.Context
import android.util.Log
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.helper.SuiviVoitureHelper
import com.example.sayaradzmb.model.version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.VersionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VersionController : SharedPreferenceInterface {

    val versionService =  ServiceBuilder.buildService(VersionService::class.java)


    fun suivreVersion(version:version,context: Context){
        val requeteAppel = versionService.suivreVersion(version.CodeVersion!!,avoirInfoUser(context))
        requeteAppel.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                if(response.isSuccessful){
                    println(response.body().toString())
                }else{
                    println("la liste modele non reconnue ${response}")

                }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.w("failConnexion","la liste modele non reconnue ${t.message}")
            }
        })
    }

    fun desuivreVersion(version: version,context: Context){
        val requeteAppel = versionService.desuivreVersion(version.CodeVersion!!,avoirIdUser(context))
        requeteAppel.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                if(response.isSuccessful){
                    println(response.body().toString())
                }else{
                    println("la liste modele non reconnue ${response}")
                }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.w("failConnexion","la liste modele non reconnue ${t.message}")
            }
        })
    }

    fun getAllVersion(context: Context,currentCodeModele:Int) : ArrayList<version>{
        var versionList : ArrayList<version>? = null
        val requeteAppel = versionService.getVersions(avoirIdUser(context),currentCodeModele)
        requeteAppel.enqueue(object : Callback<List<version>> {
            override fun onResponse(call: Call<List<version>>, response: Response<List<version>>) =
                if(response.isSuccessful){
                    println("mes version")
                    print(response.body()!!)
                    var lesVersion = response.body()!!
                    lesVersion.forEach{
                            e->versionList!!.add(e)
                    }
                }else{

                }
            override fun onFailure(call: Call<List<version>>, t: Throwable) {
                Log.w("failConnexion","la liste version non reconnue")
            }
        })
        return versionList!!
    }
}