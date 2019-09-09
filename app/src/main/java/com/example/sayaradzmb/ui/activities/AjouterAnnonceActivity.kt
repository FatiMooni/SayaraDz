package com.example.sayaradzmb.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Carburant
import com.example.sayaradzmb.model.Option
import com.example.sayaradzmb.model.fuel_type
import com.example.sayaradzmb.Repository.servics.AnnonceService
import com.example.sayaradzmb.servics.OptionService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.ui.adapter.FuelSpinnerAdapter
import com.example.sayaradzmb.viewmodel.AjouterAnnonceViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.pusher.pushnotifications.PushNotifications
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ajouter_annonce.*
import kotlinx.android.synthetic.main.content_ajouter_annonce.*
import kotlinx.android.synthetic.main.photos_annonce_dialog.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/*
* AjouterAnnonceActivity : Activity Class Manager
*
* Cette classe assure l'ajout d'une nouvelle annonces par un automobiliste
*
* */
@Suppress("DEPRECATION")
class AjouterAnnonceActivity : AppCompatActivity(), SharedPreferenceInterface {

    private var mCurrentPhotoPath: String? = null


    // variables de la vue
    private lateinit var marques: Spinner
    private lateinit var modeles: Spinner
    private lateinit var versions: Spinner
    private lateinit var options: ChipGroup

    // autres variables
    private var fileUri: Uri? = null
    private var postPath = ArrayList<String>()
    private var carburantList = ArrayList<Carburant>()
    private lateinit var optionService: OptionService

    //pour le post request
    private var idUser: BigInteger? = null
    private var codeVersion: String = ""
    private var prixVehicule: String = ""
    private var couleurVehicule: String = ""
    private var carburantVehicule: String = ""
    private var kmVehicule: String = ""
    private var descriptionVehicule: String = ""
    private var album = ArrayList<MultipartBody.Part>()

    //viewmodel instance
    lateinit var model: AjouterAnnonceViewModel

    companion object {
        const val TAKE_PHOTO_REQUEST: Int = 2
        const val PICK_PHOTO_REQUEST: Int = 1
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {

        /**
         *   préparer le contenu
         **/
        super.onCreate(savedInstanceState)

        idUser = avoirIdUser(this)
        setContentView(R.layout.activity_ajouter_annonce)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }

        /**
         *  préparer les spinner
         */

        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listOf("Choisissez une marque avant"))
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        versions = findViewById(R.id.versions_spinner)
        versions.adapter = arrayAdapter
        modeles = findViewById(R.id.modele_spinner)
        modeles.adapter = arrayAdapter
        /**
         *  lancer les traitements
         */


        //initialisation du spinner
        initCarburantSpinner(this)

        // initialiser la liste des marque
        marques = findViewById(R.id.marque_spinner)

        //create the model instance
        model = ViewModelProviders.of(this).get(AjouterAnnonceViewModel::class.java)

        //get la list des marque
        model.loadMarque()

        /** Listen for changes on the AjouterAnnonceViewModel **/
        //Marque liste
        model.getMarque().observe(this, Observer<Map<Int?, String?>> {
            spinnerMarqueSet(it!!.values.toList(), it.keys.toList())
        })
        //Modele liste
        model.getModele().observe(this, Observer<Map<Int?, String?>> {
            spinnerModeleSet(it!!.values.toList(), it.keys.toList())
        })
        //Version liste
        model.getVersion().observe(this, Observer<Map<Int?, String?>> {
            spinnerVersionSet(it!!.values.toList(), it.keys.toList())
        })

        // pour le upload des photos
        showDialog()

        // pour lancer le post request
        button_validate.setOnClickListener {
            if (input_couleur.text.isNullOrBlank() || input_prix.text.isNullOrBlank() || input_km.text.isNullOrBlank())
            {
                Toast.makeText(this,"make sure to fill all the inputs (Color, Distance ...)",Toast.LENGTH_LONG).show()
            } else if (codeVersion.isBlank()){
                Toast.makeText(this,"make sure to choose a version",Toast.LENGTH_LONG).show()
            } else if (carburantVehicule.isBlank()){
                Toast.makeText(this,"make sure to choose a fuel type from the list",Toast.LENGTH_LONG).show()
            }
            else { prixVehicule = input_prix.text.toString()
            couleurVehicule = input_couleur.text.toString()
            descriptionVehicule = input_description.text.toString()
            kmVehicule = input_km.text.toString()
            uploadImage() }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // ajouter le menu au top de l'activité
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true

            else -> super.onOptionsItemSelected(item)
        }

    }

    @SuppressLint("InflateParams")
    private fun showDialog() {
        //pour button
        add_picture.setOnClickListener {
            //Inflate the dialog --> ajouter le contenu en xml au dialog
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.photos_annonce_dialog, null)

            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            //afficher le dialog
            val chooser = mBuilder.create()
            chooser.show()
            //choisir la photo a partir de la gallerie
            mDialogView.button_gallerie.setOnClickListener {
                pickPhotoFromGallery()
            }

            //prendre une nouvelle photo
            mDialogView.dialogCancelBtn.setOnClickListener {
                askCameraPermission()
            }

            val close = mDialogView.findViewById<ImageButton>(R.id.close_btn)
            close.setOnClickListener {
                chooser.cancel()
            }


        }
    }

    /*
    Pour la creation des services
     */


    private fun createOptionService(): OptionService {
        return ServiceBuilder.buildService(OptionService::class.java)
    }

    private fun createAnnonceService(): AnnonceService {
        return ServiceBuilder.buildService(AnnonceService::class.java)
    }


    /*****
     ** Pour la manipulation des composants de la GUI // Spinners
     *****/

    //Spinner : Marque
    private fun spinnerMarqueSet(listMarque: List<String?>, listCode: List<Int?>) {

        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listMarque)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        marques.adapter = arrayAdapter

        marques.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if (listCode[position] != null) {
                    modeles = findViewById(R.id.modele_spinner)
                    modeles.isClickable = true

                    //get the models using the view model
                    model.loadModele(idUser!!, listCode[position]!!)

                } else {
                    modeles = findViewById(R.id.modele_spinner)
                    modeles.isClickable = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
                modeles = findViewById(R.id.modele_spinner)
                modeles.isClickable = false
            }
        }

    }

    //Spinner : Modele
    private fun spinnerModeleSet(listModele: List<String?>, listCode: List<Int?>) {

        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listModele)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeles.adapter = arrayAdapter

        modeles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if (listCode[position] != null) {
                    versions = findViewById(R.id.versions_spinner)
                    versions.isClickable = true
                    model.loadVersion(idUser!!, listCode[position]!!)
                } else {
                    // Code to perform some action when nothing is selected
                    versions = findViewById(R.id.versions_spinner)
                    versions.isClickable = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
                versions = findViewById(R.id.versions_spinner)
                versions.isClickable = false
            }
        }
    }

    //Spinner : Version
    private fun spinnerVersionSet(listVersion: List<String?>, listCode: List<Int?>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listVersion)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        versions.adapter = arrayAdapter

        versions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if (listCode[position] != null) {
                    codeVersion = listCode[position]!!.toString()
                    chipsOption(listCode[position]!!)
                } else {
                    print("something wrong")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }


    /*
   cette fonction initialise le spinner des carburants
    */
    private fun initCarburantSpinner(context: Context) {
        for (i in fuel_type) {
            carburantList.add(i)
        }
        val adapter = FuelSpinnerAdapter(context, carburantList)
        val spinFuel = findViewById<Spinner>(R.id.carburant_spinner)
        spinFuel.adapter = adapter

        spinFuel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                carburantVehicule = carburantList[position].title

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }


    /** TO-DO
     * check options new structure **/
    fun chipsOption(id: Int?) {

        optionService = createOptionService()
        val requestCall = optionService.getOptions(id!!)

        requestCall.enqueue(object : Callback<List<Option>> {
            override fun onResponse(call: Call<List<Option>>, response: Response<List<Option>>) =
                if (response.isSuccessful) {
                    val opt = response.body()!!
                    options = findViewById(R.id.options_chips)
                    options.removeAllViews()
                    opt.forEach { option ->
                        val chip = Chip(options.context)

                        // necessary to get single selection working
                        chip.isClickable = true
                        chip.isCheckable = true
                        chip.chipText = option.NomOption
                        options.addView(chip)
                    }

                    val chip = Chip(options.context)

                    chip.chipText = "Add"
                    chip.chipBackgroundColor =
                        ContextCompat.getColorStateList(this@AjouterAnnonceActivity, R.color.colorAccent)
                    chip.chipIcon = ContextCompat.getDrawable(this@AjouterAnnonceActivity, R.drawable.ic_add_black_24dp)


                    chip.setOnClickListener {
                        val input = findViewById<TextInputEditText>(R.id.option_input)
                        input.visibility = TextInputEditText.VISIBLE
                    }

                    options.addView(chip)

                } else {
                    Toast.makeText(this@AjouterAnnonceActivity, "There is a problem here", Toast.LENGTH_SHORT).show()
                }

            override fun onFailure(
                call: Call<List<Option>>
                , t: Throwable
            ) {
                Log.i("failure", t.message)
            }
        })

    }


    // pour le telechargement de l'image et l'envoie du post request
    private fun uploadImage() {

        //creation d'une instance sur le service des annonces
        val vService = createAnnonceService()


        //les differentes parts du post request
        val code = RequestBody.create(MediaType.parse("text/plain"), codeVersion)
        val idAu = RequestBody.create(MediaType.parse("text/plain"), idUser.toString())
        val price = RequestBody.create(MediaType.parse("text/plain"), prixVehicule)
        val desc = RequestBody.create(MediaType.parse("text/plain"), descriptionVehicule)
        val col = RequestBody.create(MediaType.parse("text/plain"), couleurVehicule)
        val dist = RequestBody.create(MediaType.parse("text/plain"), kmVehicule)
        val carb = RequestBody.create(MediaType.parse("text/plain"), carburantVehicule)

        postPath.forEach {
            //creation d'une instance fichier pour l'image séléctionnée
            val originalFile = File(it)
            val reqBody = RequestBody.create(MediaType.parse("image/*"), originalFile)
            val file = MultipartBody.Part.createFormData("imageAnnonce", originalFile.name, reqBody)
            album.add(file)
        }
        //post request
        val requestCall = vService.CreateAnnouncemet(idAu, code, price, desc, col, dist, carb, album)

        //evaluaton de la réponse
        requestCall.enqueue(object : Callback<Annonce> {
            override fun onResponse(call: Call<Annonce>, response: Response<Annonce>) {
                if (response.isSuccessful) {

                    Toast.makeText(this@AjouterAnnonceActivity, "votre annonce a été bien sauvegardée", Toast.LENGTH_LONG).show()
                    PushNotifications.addDeviceInterest("ANNONCE_${response.body()!!.idAnnonce}")
                    val intent = Intent()
                    intent.putExtra("annonce", response.body())

                    setResult(Activity.RESULT_OK , intent)
                    finish()
                } else {
                    Toast.makeText(this@AjouterAnnonceActivity, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Annonce>, t: Throwable) {
                Toast.makeText(this@AjouterAnnonceActivity, "wait  " + t.message, Toast.LENGTH_LONG).show()
                Log.i("thro", "acti" + call.isExecuted, t)
            }

        })
    }

    /*********************************************************************
     ** cette parties pour l'ajout des photos, un utilisateur peut soit****
     ** ajouter une photo à partir de la galerie                        ***
     ** ou prendre une photos directement en accedant au caméra du device**
     *********************************************************************/

    //Choisir une photo a partir de la galerie
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickImageIntent, PICK_PHOTO_REQUEST)
    }

    //override function that is called once the photo has been taken
    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val picture = ImageView(this@AjouterAnnonceActivity)
        picture.setPadding(10, 10, 10, 10)
        picture.adjustViewBounds = true
        picture.cropToPadding = true
        picture.isClickable = true

        if (resultCode == Activity.RESULT_OK
            && requestCode == TAKE_PHOTO_REQUEST
        ) {
            //To get the File for further usage
            // val auxFile = File(mCurrentPhotoPath)

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            if (postPath.size < 5)
            { postPath.add(mCurrentPhotoPath!!)
            Log.i("path capture", mCurrentPhotoPath)

            //ajouter la photo à un imageview en utilisant Picasso
            Picasso.get().load(fileUri).resize(300, 300).into(picture)
            pictures_layout.addView(picture)}
            else {
                Toast.makeText(this,"You cant insert more than five pictures",Toast.LENGTH_LONG).show()
            }

        } else if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_PHOTO_REQUEST
        ) {

            val selectedImage = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!

            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val mediaPath = cursor.getString(columnIndex)
            Log.i("path", mediaPath)
            // Set the Image in ImageView for Previewing the Media
            picture.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
            cursor.close()

            fileUri = data.data
            if (postPath.size < 5){
            postPath.add(mediaPath)

            Picasso.get().load(fileUri).resize(300, 300).into(picture)
            pictures_layout.addView(picture)}
            else {
                Toast.makeText(this,"You can't insert  more than five pictures",Toast.LENGTH_LONG).show()
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "com.example.sayaradzmb.provider",
            file
        )
        fileUri = uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST)
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    //demander les permissions de la camera
    private fun askCameraPermission() {

        //on utilise Dexter pour manipuler les differentes permissions reliées
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {
                    AlertDialog.Builder(this@AjouterAnnonceActivity)
                        .setTitle(
                            "Problemes reliés l'apareil"
                        )
                        .setMessage(
                            R.string.storage_permition_rationale_message
                        )
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(
                            android.R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .setOnDismissListener {
                            token?.cancelPermissionRequest()
                        }
                        .show()
                }

                override fun onPermissionGranted(
                    response: PermissionGrantedResponse?
                ) {
                    launchCamera()
                }

                override fun onPermissionDenied(
                    response: PermissionDeniedResponse?
                ) {
                    Snackbar.make(
                        pictures_layout,
                        "denied",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            })
            .check()
    }


}

