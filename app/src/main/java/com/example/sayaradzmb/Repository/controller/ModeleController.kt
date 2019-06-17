package com.example.sayaradzmb.Repository.controller


import android.content.Context
import android.util.Log
import android.view.Display
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.servics.ModeleService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ModeleController : SharedPreferenceInterface {

    val modeleService =  ServiceBuilder.buildService(ModeleService::class.java)

    fun suivreModele(modele: Modele,context: Context){
        val requeteAppel = modeleService.suivreModeles(modele.CodeModele!!,avoirInfoUser(context))
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

    fun desuivreModele(modele :Modele,context: Context){
        val requeteAppel = modeleService.desuivreModele(modele.CodeModele!!,avoirIdUser(context))
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

    fun getAllModele(context: Context,currentCodeMarque :Int) : ArrayList<Modele>{
        var modeleList : ArrayList<Modele>? = null
        val requeteAppel = modeleService.getModeles(avoirIdUser(context),currentCodeMarque)
        requeteAppel.enqueue(object : Callback<List<Modele>> {
            override fun onResponse(call: Call<List<Modele>>, response: Response<List<Modele>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var lesModele = response.body()!!
                    lesModele.forEach{
                            e->modeleList!!.add(e)
                    }
                }else{

                }
            override fun onFailure(call: Call<List<Modele>>, t: Throwable) {
                Log.w("failConnexion","la liste modele non reconnue ${t.message}")
            }
        })

        return modeleList!!
    }


}