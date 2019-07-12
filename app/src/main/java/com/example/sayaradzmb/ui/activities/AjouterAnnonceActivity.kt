package com.example.sayaradzmb.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.*
import com.example.sayaradzmb.servics.*
import com.example.sayaradzmb.ui.adapter.FuelSpinnerAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ajouter_annonce_activity.*
import kotlinx.android.synthetic.main.ajouter_annonce_contenu.*
import kotlinx.android.synthetic.main.photos_annonce_dialog.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/*
* AjouterAnnonceActivity : Activity Class Manager
*
* Cette classe assure l'ajout d'une nouvelle annonces par un automobiliste
*
* */
@Suppress("DEPRECATION")
class AjouterAnnonceActivity : AppCompatActivity(), SharedPreferenceInterface {


    // variables de la vue
    private lateinit var marques: Spinner
    private lateinit var modeles: Spinner
    private lateinit var versions: Spinner
    private lateinit var options: ChipGroup

    // autres variables
    private var fileUri: Uri? = null
    private var postPath = ArrayList<String>()
    private var carburantList = ArrayList<Carburant>()
    private lateinit var versionService: VersionService
    private lateinit var marqueService: MarqueService
    private lateinit var modeleService: ModeleService
    private lateinit var optionService: OptionService

    //pour le post request
    private var idUser: String? = null
    private var codeVersion: String = ""
    private var prixVehicule: String = ""
    private var couleurVehicule: String = ""
    private var carburantVehicule: String = ""
    private var kmVehicule: String = ""
    private var descriptionVehicule: String = ""
    private var album = ArrayList<MultipartBody.Part>()


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {

        /**
         *   préparer le contenu
         **/
        super.onCreate(savedInstanceState)

        idUser = avoirIdUser(this).toString()
        setContentView(R.layout.ajouter_annonce_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }

        /**
         *  préparer les spinner
         */

        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listOf("Choisir une marque avant"))
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
        recupererMarque()

        // pour le upload des photos
        showDialog()

        // pour lancer le post request
        button_validate.setOnClickListener {
            prixVehicule = input_prix.text.toString()
            couleurVehicule = input_couleur.text.toString()
            descriptionVehicule = input_description.text.toString()
            kmVehicule = input_km.text.toString()
            uploadImage()
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


    /*
    Pour la manipulation des composants de la GUI
     */

    private fun recupererMarque() {
        marqueService = createMarqueService()
        val requestCall = this.marqueService.getMarques()
        val listCodeMarque = ArrayList<Int?>()
        val listNomMarque = ArrayList<String>()

        requestCall.enqueue(object : Callback<List<Marque>> {

            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) =
                if (response.isSuccessful) {
                    val list = response.body()!!

                    listCodeMarque.add(0, null)
                    listNomMarque.add(0, "Choisissez une Marque")

                    //récuperer les données et les mettre dans une liste apart

                    list.forEach { e ->
                        listCodeMarque.add(e.CodeMarque!!)
                        listNomMarque.add(e.NomMarque!!)
                    }

                    spinnerMarqueSet(listNomMarque, listCodeMarque)

                } else {
                    Toast.makeText(
                        this@AjouterAnnonceActivity, "there is a problem in our server",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.i("Getting marks onFailure", t.message)
            }
        })
    }

    fun spinnerMarqueSet(listMarque: List<String>, listCode: List<Int?>) {

        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listMarque)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        marques.adapter = arrayAdapter

        marques.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if (listCode[position] != null) {
                    modeles = findViewById(R.id.modele_spinner)
                    modeles.isClickable = true
                    recupererModele(listCode[position]!!)

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


    // envoyer la requete de modeles
    fun recupererModele(id: Int?) {
        modeleService = createModeleServce()
        val requestCall = modeleService.getModeles(avoirIdUser(this@AjouterAnnonceActivity), id!!)
        val listCodeModele = ArrayList<Int?>()
        val listNomModele = ArrayList<String>()

        requestCall.enqueue(object : Callback<List<Modele>> {

            override fun onResponse(call: Call<List<Modele>>, response: Response<List<Modele>>) =
                if (response.isSuccessful) {
                    val list = response.body()!!
                    listCodeModele.add(0, null)
                    listNomModele.add(0, "Choisissez un Modele")

                    list.forEach { e ->
                        listCodeModele.add(e.CodeModele!!)
                        listNomModele.add(e.NomModele!!)
                    }

                    spinnerModeleSet(listNomModele, listCodeModele)
                } else {
                    Toast.makeText(
                        this@AjouterAnnonceActivity, "there is a problem in our server",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            override fun onFailure(call: Call<List<Modele>>, t: Throwable) {
            }
        })
    }

    fun spinnerModeleSet(listModele: List<String>, listCode: List<Int?>) {

        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, listModele)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeles.adapter = arrayAdapter

        modeles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if (listCode[position] != null) {
                    versions = findViewById(R.id.versions_spinner)
                    versions.isClickable = true
                    recupererVersion(listCode[position]!!)
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


    // envoyer la requete de modeles
    fun recupererVersion(id: Int?) {
        versionService = createversionService()
        val requestCall = versionService.getVersions(avoirIdUser(this@AjouterAnnonceActivity), id!!)
        val listCodeVersion = ArrayList<Int?>()
        val listNomVersion = ArrayList<String>()

        requestCall.enqueue(object : Callback<List<Version>> {

            override fun onResponse(call: Call<List<Version>>, response: Response<List<Version>>) =
                if (response.isSuccessful) {
                    val list = response.body()!!
                    listCodeVersion.add(0, null)
                    listNomVersion.add(0, "Choisissez une Version")

                    list.forEach { e ->
                        listCodeVersion.add(e.CodeVersion!!)
                        listNomVersion.add(e.NomVersion!!)
                    }
                    spinnerVersionSet(listNomVersion, listCodeVersion)

                } else {
                    Toast.makeText(
                        this@AjouterAnnonceActivity, "there is a problem in our server",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            override fun onFailure(call: Call<List<Version>>, t: Throwable) {
            }
        })
    }

    fun spinnerVersionSet(listVersion: List<String>, listCode: List<Int?>) {
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


    /*
    cette parties pour l'ajout des photos, un utilisateur peut soit ajouter une photo à partir de la galerie
    ou prendre une photos directement en accedant au caméra du device
     */

    //Choisir une photo a partir de la galerie
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickImageIntent, AppConstants.PICK_PHOTO_REQUEST)
    }

    //override function that is called once the photo has been taken
    @SuppressLint("Recycle")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val picture = ImageView(this@AjouterAnnonceActivity)
        picture.setPadding(10, 10, 10, 10)
        picture.adjustViewBounds = true
        picture.cropToPadding = true
        picture.isClickable = true

        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.TAKE_PHOTO_REQUEST
        ) {


            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            postPath.add(fileUri!!.path)

            //ajouter la photo à un imageview en utilisant Picasso
            Picasso.get().load(fileUri).resize(300, 300).into(picture)
            pictures_layout.addView(picture)

        } else if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.PICK_PHOTO_REQUEST
        ) {

            val selectedImage = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val mediaPath = cursor.getString(columnIndex)

            // Set the Image in ImageView for Previewing the Media
            picture.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
            cursor.close()

            postPath.add(mediaPath)
            fileUri = data.data

            Picasso.get().load(fileUri).resize(300, 300).into(picture)
            pictures_layout.addView(picture)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            startActivityForResult(intent, AppConstants.TAKE_PHOTO_REQUEST)
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

    // pour le telechargement de l'image et l'envoie du post request
    private fun uploadImage() {

        //creation d'une instance sur le service des annonces
        val vService = createAnnonceService()


        //les differentes parts du post request
        val code = RequestBody.create(MediaType.parse("text/plain"), codeVersion)
        val idAu = RequestBody.create(MediaType.parse("text/plain"), idUser!!)
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
                    Toast.makeText(this@AjouterAnnonceActivity, response.message(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@AjouterAnnonceActivity, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Annonce>, t: Throwable) {
                Toast.makeText(this@AjouterAnnonceActivity, "wait  " + t.message, Toast.LENGTH_LONG).show()
            }

        })
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

}


object AppConstants {
    const val TAKE_PHOTO_REQUEST: Int = 2
    const val PICK_PHOTO_REQUEST: Int = 1
}