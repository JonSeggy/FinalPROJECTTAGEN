package com.example.finalprojecttagen

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegistrationScreen : AppCompatActivity() {
    private lateinit var btn_register2: Button;
    private lateinit var btn_retStart2: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_register2 = findViewById(R.id.btn_register2);
        this.btn_retStart2 = findViewById(R.id.btn_retStart2);


        btn_register2.setOnClickListener({
            val toStart = Intent(applicationContext, MainActivity::class.java);
            val regEmailText: EditText = findViewById(R.id.regEmail);
            val regEmail: String = regEmailText.text.toString();
            val regPasswordText: EditText = findViewById(R.id.regPassword);
            val regPassword: String = regPasswordText.text.toString();
            registerUser(regEmail, regPassword);
            startActivity(toStart);
        })

        btn_retStart2.setOnClickListener({
            val toStart = Intent(applicationContext, MainActivity::class.java);
            startActivity(toStart)
        })
    }

    val url = "https://www.jwuclasses.com/ugly/register"

    private fun registerUser(regEmail: String, regPassword: String) {
        Log.d("Register", "Email: $regEmail, Password: $regPassword")
        val infoPassed = JSONObject().apply {
                put("email", regEmail)
                put("password", regPassword)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, infoPassed, {
            response ->
                val success = response.getInt("success")
                Log.d("Register", "$success")

                if (success == 1) {
                    Toast.makeText(this, "Registration Success", Toast.LENGTH_LONG).show()
                } else {
                    val errorMessage = response.getString("errormessage")
                    Toast.makeText(this, "Registration Failed: $errorMessage", Toast.LENGTH_LONG).show()
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
}
