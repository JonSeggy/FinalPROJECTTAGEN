package com.example.finalprojecttagen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONArray
import org.json.JSONObject

class AddPet : AppCompatActivity() {
    private lateinit var PetName: EditText
    private lateinit var PetDesc: EditText
    private lateinit var PetURL: EditText
    private lateinit var btn_addNewPet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_pet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_addNewPet = findViewById(R.id.btn_addNewPet)
        this.PetURL = findViewById(R.id.url_addPet)
        this.PetDesc = findViewById(R.id.desc_addPet)
        this.PetName = findViewById(R.id.name_addPet)

        btn_addNewPet.setOnClickListener{
            val name = PetName.text.toString()
            val url = PetURL.text.toString()
            val desc = PetDesc.text.toString()


            val pet = Pet(null, name, url, desc)
            savePet(pet)

            val tomyPets = Intent(applicationContext, MyPets::class.java)
            Log.d("Pet", "name: $name & desc: $desc & url: $url")
            startActivity(tomyPets)

        }
    }

    private fun tokenGet(): String? {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(this, "secured_key", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        return sharedPreferences.getString("token", null)
    }

    private fun savePet(pet: Pet){
        val sharedPreferences = getSharedPreferences("pet_database", Context.MODE_PRIVATE)
        val myEditor = sharedPreferences.edit()

        val existingPets = sharedPreferences.getString("listOfPets", null)
        val myArrayPets = if (existingPets.isNullOrEmpty()) JSONArray() else JSONArray(existingPets)

        val petID = myArrayPets.length() + 1
        val petData = JSONObject().apply {
            put("id", petID)
            put("url", pet.url)
            put("name", pet.name)
            put("description", pet.desc)
        }

        myArrayPets.put(petData)

        myEditor.putString("listOfPets", myArrayPets.toString())
        myEditor.apply()
    }
}