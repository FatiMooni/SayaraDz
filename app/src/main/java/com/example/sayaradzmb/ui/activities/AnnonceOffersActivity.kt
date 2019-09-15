package com.example.sayaradzmb.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.ui.adapter.CarImageSliderAdapter
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter
import com.example.sayaradzmb.viewmodel.AnnonceOffersViewModel
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderPager
import com.smarteist.autoimageslider.SliderView
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
            override fun onButtonClickItem(value: Comparable<*>, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPopupMenuRequested(value: Comparable<*>, view: View, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickItem(id: Int, state: String, position: Int) {
                Toast.makeText(this@AnnonceOffersActivity, "you did it :::", Toast.LENGTH_LONG)
                    .show()
                model.updateOffersList(id, state, position)
                if (state == "accepter") {
                    Snackbar.make(rv_offers, "you have been accepeted", Snackbar.LENGTH_LONG).show()

                }
            }
        })

        model = ViewModelProviders.of(this).get(AnnonceOffersViewModel::class.java)
        model.getUsedCars().observe(this,
            Observer<List<Offre>> { t ->
                offerAdapter!!.swapData(t!!)
                Toast.makeText(this@AnnonceOffersActivity, t.toString(), Toast.LENGTH_LONG).show()

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


    }

    private fun extraInfoLoader() {
        when {
            expandable_layout.isExpanded -> expandable_layout.collapse()
            car_img_viewer.isExpanded -> car_img_viewer.collapse()
            else -> {
                expandable_layout.expand()
                car_img_viewer.expand()
            }
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
        car_announce_title.text =
            version.NomMarque.plus(" " + version.NomModele).plus(" " + version.NomVersion)
        car_announce_price.text = item.Prix.plus(" DZ")
        car_announce_offer.text = item.NombreOffres.toString().plus(" offres")
        KM_title.text = item.Km
        findViewById<TextView>(R.id.color_title).text = item.Couleur
        findViewById<TextView>(R.id.year_title).text = item.Annee.toString()
        findViewById<TextView>(R.id.type_title).text = item.Carburant
       /* val pAdapter = VehiculeImageAdapter(this, item.images!!)
        findViewById<ViewPager>(R.id.img_viewer).adapter = pAdapter*/


        var sliderView = findViewById<SliderView>(R.id.imageSlider)
        val adapter = CarImageSliderAdapter(this, item.images!!)
        sliderView.sliderAdapter =  adapter
        sliderView.startAutoCycle()
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
        sliderView.indicatorSelectedColor = Color.rgb(59,16,109)
        sliderView.indicatorUnselectedColor = Color.GRAY;
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
        sliderView.startAutoCycle()
       }



}
