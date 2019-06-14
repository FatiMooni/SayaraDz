package com.example.sayaradzmb.activities.Fragments

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alespero.expandablecardview.ExpandableCardView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.adapter.MarqueAdapter
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.fuel_type
import com.example.sayaradzmb.model.version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.ViheculeService
import kotlinx.android.synthetic.main.item_carburant.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.zip.Inflater
import android.widget.Spinner
import android.widget.ArrayAdapter
import java.util.*
import kotlin.collections.ArrayList


class OccasionFragment : Fragment(),RecycleViewHelper {

    override var itemRecycleView : RecyclerView? = null
    private var marqueList = ArrayList<Marque>()
    private var marqueAdapter : MarqueAdapter? = null
    private var featuresDropDown : ExpandableCardView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var myView = inflater.inflate(R.layout.fragement_occasion,container,false)

        //prepare the recycle view + marque list
        init(myView)
        getMarque()

        initFeaturesLayout(myView,inflater)
        initYearsSpinner(myView)

        //retourner le fragment
        return myView
    }



    /**
     * l'interface qui aide a envoyer des donnee d'un fragment a l'activity
     */
    interface OnSearchPressed{
        fun envoyerFragment(int : Int,version : version)
    }

    /**
     * onAttach methode overriding
     */
   /* override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as Activity
        onSearchPressed = activity as OnSearchPressed
    }*/



    /**
     * la fonctin qui va faire l'appel a lapi pour avoir les marque
     */
    private fun getMarque(){
        val vService = ServiceBuilder.buildService(ViheculeService::class.java)
        val requeteAppel = vService.getMarques()
        requeteAppel.enqueue(object : Callback<List<Marque>> {
            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var lesMarque = response.body()!!
                    lesMarque.forEach{
                            e->marqueList.add(e)
                    }
                }else{

                }
            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
            }
        })
    }
    /**
     * les fonction d'essai
     */

    private fun init(view : View ){
        marqueAdapter = MarqueAdapter(marqueList,view.context,view)
        initLineaire(view,R.id.imd_rv_marque,
            LinearLayoutManager.VERTICAL,marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun initFeaturesLayout(view : View, inf : LayoutInflater){

        var position = 0
        featuresDropDown = view.findViewById(R.id.other_features)
        var holder = view.findViewById<LinearLayoutCompat>(R.id.carburant_type_holder)
        for (carb in fuel_type) {

            var carburantView = inf.inflate(R.layout.item_carburant, null)
            carburantView.findViewById<TextView>(R.id.title_carb_type).text = carb.title
            carburantView.findViewById<ImageView>(R.id.aprev_carb_type).setImageDrawable(view.context.resources.getDrawable(carb.image,null))
            holder.addView(carburantView)
            holder.getChildAt(0).isFocusable = true

            carburantView.setOnClickListener {
                holder.getChildAt(position).background = view.context.resources.getDrawable(R.drawable.white_radius,null)
                carburantView.background = view.context.resources.getDrawable(R.drawable.selected_bg,null)
            }


        }


    }

    fun  initYearsSpinner(view : View){
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1980..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item, years)

        val spinYear = view.findViewById(R.id.years_picker) as Spinner
        spinYear.adapter = adapter
    }



}

