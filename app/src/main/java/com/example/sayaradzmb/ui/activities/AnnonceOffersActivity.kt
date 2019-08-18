package com.example.sayaradzmb.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter
import com.example.sayaradzmb.viewmodel.AnnonceOffersViewModel
import kotlinx.android.synthetic.main.activity_annonce_offers.*
import kotlinx.android.synthetic.main.content_annonce_offers.*

class AnnonceOffersActivity : AppCompatActivity() {

    private var offersList = ArrayList<Comparable<*>>()
    private var offerAdapter: CustomCardsAdapter? = null
    private var model = AnnonceOffersViewModel()
    private var annonce: Annonce? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annonce_offers)
        setSupportActionBar(toolbar)


        prepareRecyclerView()

        offerAdapter!!.setOnItemClickListener(object : CustomCardsAdapter.OnClickItemListener {
            override fun onPopupMenuRequested(value: Comparable<*>, view: View, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickItem(id: Int, state: String, position: Int) {
                Toast.makeText(this@AnnonceOffersActivity, "you did it :::", Toast.LENGTH_LONG).show()
                model.updateOffersList(id, state, position)
            }
        })

        model = ViewModelProviders.of(this).get(AnnonceOffersViewModel::class.java)
        model.getUsedCars().observe(this,
            Observer<List<Offre>> { t ->
                        offerAdapter!!.swapData(t!!)
                Toast.makeText(this@AnnonceOffersActivity,t.toString(),Toast.LENGTH_LONG).show()

            })

        val intent = intent
        if (intent.hasExtra("annonce")) {
            annonce = intent.getParcelableExtra("annonce")
            setAnnounceData(annonce!!)
            model.loadUsedCars(annonce!!.idAnnonce!!)
        }


        expand_btn.setOnClickListener {
            extraInfoLoader()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                Toast.makeText(this@AnnonceOffersActivity,"UP",Toast.LENGTH_LONG).show()
                return true
            }

            override fun onSwiped(holder: RecyclerView.ViewHolder, p1: Int) {
                val item = offerAdapter!!.getItemAt(holder.adapterPosition)
                if (item is Offre)
                   model.updateOffersList(item.idOffre,"refuser" , holder.adapterPosition)
                else throw IllegalArgumentException("Invalid view type")
            }

        }).attachToRecyclerView(rv_offers)



    }

    private fun extraInfoLoader() {
        when {
            expandable_layout.isExpanded -> expandable_layout.collapse()
            car_img_viewer.isExpanded -> car_img_viewer.collapse()
            else -> {expandable_layout.expand()
                      car_img_viewer.expand()}
        }
    }

    private fun prepareRecyclerView() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = findViewById<RecyclerView>(R.id.rv_offers)
        adapter.layoutManager = layout
        offerAdapter = CustomCardsAdapter(this, offersList)
        adapter.adapter = offerAdapter
    }

    private fun setAnnounceData(item: Annonce) {
        val version = item.version!!
        car_announce_title.text = version.NomMarque.plus(" " + version.NomModele).plus(" " + version.NomVersion)
        car_announce_price.text = item.Prix
        car_announce_offer.text = item.NombreOffres.toString()
        KM_title.text = item.Km
        findViewById<TextView>(R.id.color_title).text = item.Couleur
        findViewById<TextView>(R.id.year_title).text = "2002"
        findViewById<TextView>(R.id.type_title).text = item.Carburant
        val pAdapter = VehiculeImageAdapter(this, annonce!!.images!!)
        findViewById<ViewPager>(R.id.img_viewer).adapter = pAdapter

    }

}
