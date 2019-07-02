package com.example.sayaradzmb.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import kotlinx.android.synthetic.main.ajouter_annonce_activity.*

class OffreActivity :  AppCompatActivity() , SharedPreferenceInterface {
    /**
     *
     */
    private var idUser : String? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        idUser = avoirIdUser(this).toString()
        setContentView(R.layout.ajouter_annonce_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
    }

}