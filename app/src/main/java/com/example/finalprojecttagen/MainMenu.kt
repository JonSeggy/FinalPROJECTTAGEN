package com.example.finalprojecttagen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MainMenu : AppCompatActivity() {

    private lateinit var btn_logout: Button
    private lateinit var btn_leaderboards: Button
    private lateinit var btn_myPets: Button
    private lateinit var btn_vote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_logout = findViewById(R.id.btn_logout)
        this.btn_leaderboards = findViewById(R.id.btn_leaderboards)
        this.btn_myPets = findViewById(R.id.btn_myPets)
        this.btn_vote = findViewById(R.id.btn_vote)
        val key = tokenGet()

        btn_leaderboards.setOnClickListener{
            val toLeaderboard = Intent(applicationContext, Leaderboard::class.java);
            startActivity(toLeaderboard)
        }

        btn_logout.setOnClickListener{
            tokenDel()
            Toast.makeText(this, "Logout Success", Toast.LENGTH_LONG).show()
            val toStart = Intent(applicationContext, LoginScreen::class.java);
            startActivity(toStart)
        }

        btn_myPets.setOnClickListener{
            val toMyPets = Intent(applicationContext, MyPets::class.java);
            Log.d("Token", "Token: $key")
            startActivity(toMyPets)
            Log.d("debug", "the button was clicked")
        }

        btn_vote.setOnClickListener{
            val toVoting = Intent(applicationContext, PetVoting::class.java)
            startActivity(toVoting)
        }
    }

    private fun tokenDel() {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(this, "secured_key", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        sharedPreferences.edit().remove("token").apply()
    }

    private fun tokenGet(): String? {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(this, "secured_key", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        return sharedPreferences.getString("token", null)
    }
}