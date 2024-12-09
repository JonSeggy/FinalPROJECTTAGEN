package com.example.finalprojecttagen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

class PetVoting : AppCompatActivity() {
    private lateinit var btn_home: Button
    private lateinit var btn_submit: Button
    private lateinit var nameBox: TextView
    private lateinit var descBox: TextView
    private lateinit var randomPetID: TextView
    private lateinit var dogImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pet_voting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.btn_home = findViewById(R.id.btn_home)
        this.btn_submit = findViewById(R.id.btn_submit)
        this.nameBox = findViewById(R.id.nameBox)
        this.descBox = findViewById(R.id.descBox)
        this.randomPetID = findViewById(R.id.randomPetID)
        this.dogImage = findViewById(R.id.dogImage)
        getRandomDog()


        btn_home.setOnClickListener{
            val backHome = Intent(applicationContext, MainMenu::class.java)
            startActivity(backHome)
        }

        btn_submit.setOnClickListener{
            val token = getToken()
            val backHome = Intent(applicationContext, MainMenu::class.java)
            val petsID = getID()
            sendVote(token.toString(), petsID, 2)
            startActivity(backHome)
        }
    }

    private fun getRandomDog() {
        val url = "https://www.jwuclasses.com/ugly/getvote"
        val getDogInfo = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val dogID = response.getInt("id")
            Log.d("PetVoting", "Current JSON Object: $dogID")
            val dogName = response.getString("name")
            val dogDesc = response.getString("desc")
            val dogURL = response.getString("url")

            nameBox.text = dogName
            descBox.text = dogDesc
            randomPetID.text = dogID.toString()

            Picasso.get().load(dogURL).into(dogImage)
        },
            { error ->
                Log.e("GETVote", "Error has occurred")
                Toast.makeText(this, "Failed to grab Pet", Toast.LENGTH_LONG).show()
            }
        )
        Volley.newRequestQueue(this).add(getDogInfo)
    }

    private fun sendVote(token: String, id: Unit, rating: Int){
        val url = "https://www.jwuclasses.com/ugly/savevote"
        val myQueue = Volley.newRequestQueue(this)

        val myInfo = JSONObject().apply{
            put("id", id)
            put("token", token)
            put("rating", rating)
        }

        val postRequest = JsonObjectRequest(Request.Method.POST, url, myInfo, {
            reponse ->
                Log.d("PostRequest", "Success!")
        },
            { error ->
                Log.d("PostRequest", "Error has occurred ")
            }
        )
        myQueue.add(postRequest)
    }

    private fun getToken(): String? {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secured_key",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences.getString("token", null)
    }

    private fun getID(){
        JSONObject().getInt("id")
    }
}