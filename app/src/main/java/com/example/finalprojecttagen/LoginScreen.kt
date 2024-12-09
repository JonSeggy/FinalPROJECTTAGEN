package com.example.finalprojecttagen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class LoginScreen : AppCompatActivity() {

    private lateinit var btn_login2: Button;
    private lateinit var btn_retStart: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_login2 = findViewById(R.id.btn_login2);
        this.btn_retStart = findViewById(R.id.btn_retStart);

        btn_login2.setOnClickListener({
            val toMainMenu = Intent(applicationContext, MainMenu::class.java);
            val loginEmailTxt: EditText = findViewById(R.id.loginEmail);
            val loginEmailStr: String = loginEmailTxt.text.toString();
            val passEmailTxt: EditText = findViewById(R.id.loginPassword);
            val passEmailStr: String = passEmailTxt.text.toString();
            getLogin(loginEmailStr, passEmailStr)
            startActivity(toMainMenu);
        })

        btn_retStart.setOnClickListener({
            val toStart = Intent(applicationContext, MainActivity::class.java);
            startActivity(toStart);
        })
    }

    private fun getLogin(loginEmailStr: String, passEmailStr: String ){
        val url = "https://www.jwuclasses.com/ugly/login?email=$loginEmailStr&password=$passEmailStr"
        Log.d("Login", "Email: $loginEmailStr Password: $passEmailStr")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, {
            response ->
            val success = response.getInt("success")
            Log.d("Login", "$success")

            if (success == 1) {
                val token = response.getString("token")
                tokenSave(token)
                Log.d("Token", "Token: $token")
                Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()

            } else {
                val errorMessage = response.getString("errormessage")
                Toast.makeText(this, "Login Failed: $errorMessage", Toast.LENGTH_LONG).show()
            }
        },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error: ${error.message ?: "Unknown error"}", Toast.LENGTH_LONG).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }

    private fun tokenSave(token: String){
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(this, "secured_key", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        sharedPreferences.edit().putString("token",token).apply()
    }



}