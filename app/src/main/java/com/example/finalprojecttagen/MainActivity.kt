package com.example.finalprojecttagen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btn_login: Button;
    private lateinit var btn_register: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.btn_login = findViewById(R.id.btn_login);
        this.btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener({
            val toLogin = Intent(applicationContext, LoginScreen::class.java);
            startActivity(toLogin);
        })

        btn_register.setOnClickListener({
            val toRegister = Intent(applicationContext, RegistrationScreen::class.java);
            startActivity(toRegister);
        })
    }
}