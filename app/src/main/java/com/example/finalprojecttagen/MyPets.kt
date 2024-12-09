package com.example.finalprojecttagen

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONArray

class MyPets : AppCompatActivity() {
    private lateinit var btn_addPet: Button
    private lateinit var btn_delPet: Button
    private lateinit var tableOfPets: TableLayout
    private lateinit var btn_OuttaHere: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_pets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_addPet = findViewById(R.id.btn_addPet)
        this.btn_OuttaHere = findViewById(R.id.btn_OuttaHere)
        this.tableOfPets = findViewById(R.id.tableOfPets)
        this.btn_delPet = findViewById(R.id.btn_delPet)
        getPets()

        btn_addPet.setOnClickListener{
            val to_addPet = Intent(applicationContext, AddPet::class.java);
            startActivity(to_addPet);
        }

        btn_delPet.setOnClickListener{
            val to_delPet = Intent(applicationContext, DeletePet::class.java);
            startActivity(to_delPet);
        }

        btn_OuttaHere.setOnClickListener{
            val backOut = Intent(applicationContext, MyPets::class.java)
            startActivity(backOut)
        }
    }

    private fun getPets(){
        val sharedPreferences = getSharedPreferences("pet_database", Context.MODE_PRIVATE)
        val listPetString = sharedPreferences.getString("listOfPets", "[]")

        val myPetArray = JSONArray(listPetString)
        infoIntoTable(myPetArray)
    }

    private fun infoIntoTable(myPetArray: JSONArray){
        tableOfPets.removeAllViews()

        val initiateRow = TableRow(this).apply{
            addView(textCreation("Id", true))
            addView(textCreation("Image", true))
            addView(textCreation("Name", true))
            addView(textCreation("Description", true))
        }
        tableOfPets.addView(initiateRow)

        for(i in 0 until myPetArray.length()){
            val currentPet = myPetArray.getJSONObject(i)
            val rows = TableRow(this).apply{
                addView(textCreation((i+1).toString()))
                addView(imageCreation(currentPet.getString("url")))
                addView(textCreation(currentPet.getString("name")))
                addView(textCreation(currentPet.getString("description")))
            }
            tableOfPets.addView(rows)
        }
    }
    private fun imageCreation(imageUrl: String): ImageView{
        val image = ImageView(this)
        Picasso.get().load(imageUrl).into(image)

        val dimensions = TableRow.LayoutParams(100,100)
        image.layoutParams = dimensions
        return image
    }

    private fun textCreation(text: String, isHeader: Boolean = false): TextView {
        return TextView(this).apply{
            this.text = text
            setPadding(16,16,16,16)
            if(isHeader) {
                setTextColor(resources.getColor(android.R.color.black))
                textSize = 18f
            }
        }
    }

}