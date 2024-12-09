package com.example.finalprojecttagen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONArray

class DeletePet : AppCompatActivity() {
    private lateinit var btn_delete: Button
    private lateinit var petID: EditText
    private lateinit var btn_return: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_pet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_delete = findViewById(R.id.btn_delete)
        this.petID = findViewById(R.id.petID)
        this.btn_return = findViewById(R.id.btn_return)

        btn_return.setOnClickListener{
            val toPets = Intent(applicationContext, MyPets::class.java)
            startActivity(toPets)
        }

        btn_delete.setOnClickListener{
            val idText = petID.text.toString()
            delPet(idText.toInt())
            val toPets = Intent(applicationContext, MyPets::class.java)
            startActivity(toPets)
        }
    }

    private fun delPet(petID: Int){
        val sharedPreferences = getSharedPreferences("pet_database", Context.MODE_PRIVATE)
        val myEditor = sharedPreferences.edit()

        val existingPets = sharedPreferences.getString("listOfPets", null)
        val myArray =  JSONArray(existingPets)

        myArray.remove(petID - 1)

        myEditor.putString("listOfPets", myArray.toString())
        myEditor.apply()
        Toast.makeText(this,"Bye Bye Pet", Toast.LENGTH_LONG).show()
    }
}