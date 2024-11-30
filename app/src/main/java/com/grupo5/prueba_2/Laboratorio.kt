package com.grupo5.prueba_2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class Laboratorio : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboratorio)

        val reliquias : ListView = findViewById(R.id.reliquias)
        val palabras = arrayOf("hola", "esto", "es", "una", "prueba")

        // Crear el adaptador y asociarlo con el ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, palabras)
        reliquias.adapter = adapter

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged()

        // Configurar acción al hacer clic en un elemento
        reliquias.setOnItemClickListener { _, _, position, _ ->
            val elementoSeleccionado = palabras[position]
            //mostrar vista de la reliquia
            Toast.makeText(this, "Seleccionaste: $elementoSeleccionado", Toast.LENGTH_SHORT).show()
        }
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                val intent = Intent(this@Laboratorio, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }

}