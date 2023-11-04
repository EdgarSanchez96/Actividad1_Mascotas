package com.example.actividad1_mascotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_NoTitleBar);
        setContentView(R.layout.activity_main)

        // Iniciar una tarea diferida para abrir ListPetsActivity después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ListPetsActivity::class.java)
            startActivity(intent)
            finish()  // Esto cierra la actividad actual para que no puedas volver atrás a la pantalla de inicio
        }, 3000) // 3000 milisegundos (3 segundos)


    }

}