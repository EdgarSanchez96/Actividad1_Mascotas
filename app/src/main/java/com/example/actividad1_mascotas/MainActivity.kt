package com.example.actividad1_mascotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_NoTitleBar);
        setContentView(R.layout.activity_main)
        copyRawJsonToInternalStorage()

        // Iniciar una tarea diferida para abrir ListPetsActivity después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ListPetsActivity::class.java)
            startActivity(intent)
            finish()  // Esto cierra la actividad actual para que no puedas volver atrás a la pantalla de inicio
        }, 3000) // 3000 milisegundos (3 segundos)

    }

    private fun copyRawJsonToInternalStorage() {
        val inputStream = resources.openRawResource(R.raw.pets)
        val outputStream: FileOutputStream

        val fileName = "pets.json"
        val file = File(filesDir, fileName)

        try {
            outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.close()
            inputStream.close()

            // Aquí puedes mostrar un mensaje de éxito o realizar otras acciones necesarias
            Toast.makeText(this, "Archivo actualizado en el almacenamiento interno", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            // Aquí puedes mostrar un mensaje de error en caso de que ocurra una excepción
            Toast.makeText(this, "Error al actualizar el archivo", Toast.LENGTH_SHORT).show()
        }
    }


}