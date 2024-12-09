package com.example.finalprojecttagen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

class Leaderboard : AppCompatActivity() {
    private lateinit var btn_goBack: Button
    private lateinit var leaderboardTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_goBack = findViewById(R.id.btn_goBack)
        this.leaderboardTable = findViewById(R.id.tableLeaderboard)
        getLeaderboardData()

        btn_goBack.setOnClickListener{
            val getOuttaHere = Intent(applicationContext, MainMenu::class.java)
            startActivity(getOuttaHere)
        }
    }

    private fun getLeaderboardData(){
        val url = "https://www.jwuclasses.com/ugly/leaderboard"

        val GETpets = JsonObjectRequest(Request.Method.GET,url,null, {
            response ->


                if (response == null) {
                    Log.e("Leaderboard", "Response is null")
                    Toast.makeText(this, "No data received", Toast.LENGTH_LONG).show()
                    return@JsonObjectRequest
                }

                val arrayObject = JSONObject(response.toString())
                val leaderboardArray = arrayObject.getJSONArray("pets")
                leaderboardTable.removeAllViews()

                val headers = TableRow(this)
                headers.addView(createTextView("ID"))
                headers.addView(createTextView("Pet Name"))
                headers.addView(createTextView("Image"))
                headers.addView(createTextView("Avg Rating"))
                headers.addView(createTextView("Total Votes"))
                leaderboardTable.addView(headers)

                for (i in 0 until leaderboardArray.length()) {
                    val petObject = leaderboardArray.getJSONObject(i)
                    val petId = petObject.getString("id")
                    val petName = petObject.getString("name")
                    val imageUrl = petObject.getString("url")
                    val avgRating = petObject.getDouble("rating")
                    val totalVotes = petObject.getInt("votes")

                    val row = TableRow(this)
                    row.addView(createTextView(petId))
                    row.addView(createTextView(petName))
                    row.addView(createImageView(imageUrl))
                    row.addView(createTextView(avgRating.toString()))
                    row.addView(createTextView(totalVotes.toString()))

                    leaderboardTable.addView(row)
                }
            },
            { error ->
                Log.e("VolleyError", "Error: ${error.localizedMessage}")
                Toast.makeText(this, "Error fetching leaderboard", Toast.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(GETpets)
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.setPadding(16, 16, 16, 16)
        return textView
    }

    private fun createImageView(url: String): ImageView {
        val imageView = ImageView(this)
        Picasso.get().load(url).into(imageView)
        imageView.layoutParams = TableRow.LayoutParams(100, 100)
        return imageView
    }
}