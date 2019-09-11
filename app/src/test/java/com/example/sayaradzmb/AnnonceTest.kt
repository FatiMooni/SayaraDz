package com.example.sayaradzmb


import com.example.sayaradzmb.Repository.servics.AnnonceService
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException


class AnnonceTest {

    lateinit var mockWebServer : MockWebServer
    lateinit var retrofit : Retrofit
    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        mockWebServer.start()


        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Test
    @Throws(IOException::class)
    fun `Get Announce Test`() {



        //Set a response for retrofit to handle.
        //This is a simple response from the original server .
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(
                        "[{"+
                        "\"idAnnonce\": 54, \"Prix\": \"2290000 \","+
                        "\"idAutomobiliste\": \"380466752766558\","+
                        "\"CodeVersion\": 1,"+
                        "\"Couleur\": \"Bleu\","+
                        "\"Km\": \"20000\","+
                        "\"Carburant\": \"Diesel\","+
                        "\"Annee\": 2019,"+
                        "\"Description\": \"Une trés belle voiture!\","+
                        "\"NombreOffres\": 2,"+
                        "\"images\": [ { \"CheminImage\": \"https://www.africargus.com/wp-content/uploads/2018/07/Essai-Clio-GT-Line-Algerie-Africargus-02.jpg\" }]"
                        +"}]"
            )
        mockWebServer.enqueue(response)

        //create the service
        val service = retrofit.create<AnnonceService>(AnnonceService::class.java)

        //With your service created you can now call its method that should
        //consume the MockResponse above. You can then use the desired
        //assertion to check if the result is as expected. For example:
        val call = service.GetAnnouncement("380466752766558")
        val responseReal = call.execute()

        //Testing
        assertTrue(responseReal.isSuccessful)
        assertTrue(responseReal.body()!!.size == 1)
        assertEquals("380466752766558", responseReal.body()!![0].idAutomobiliste)

    }



    @After
    fun tearUp(){
        //Finish web server
        mockWebServer.shutdown()
    }

    private fun getJson(path: String): String {
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}