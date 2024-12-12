package com.grupo5.prueba_2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.animation.AlphaAnimation
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.File
import kotlin.collections.mapOf

class Reliquias : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reliquias)

        val nombres = intent.getStringArrayListExtra("nombres")
        val descripciones = intent.getStringArrayListExtra("descripciones")
        var posicion = intent.getIntExtra("posicion", 0)

        val nombre:TextView = findViewById(R.id.nombre)
        val descripcion:TextView = findViewById(R.id.Descripcion)
        val imagen:TextView = findViewById(R.id.Imagen)

        descripcion.movementMethod=ScrollingMovementMethod()
        val imagenesPorNombre = mapOf(
            "Broche de perro" to R.drawable.broche_de_perro,
            "Pomo de puñal" to R.drawable.pomo_de_punal,
            "Cajitas de barro" to R.drawable.cajitas_de_barro,
            "Sonajas" to R.drawable.sonajas,
            "Tintinábulas" to R.drawable.tintinabulas,
            "Estela de clunia" to R.drawable.estela_de_clunia,
            "Denario de Baskunes" to R.drawable.denario_de_baskunes,
            "Tapa de horno" to R.drawable.tapa_de_horno,
            "Zoomorfo vacceo" to R.drawable.zoomorfo_vacceo
        )


        if (nombres != null && descripciones != null) {
            nombre.text = nombres.get(posicion)
            descripcion.text=descripciones[posicion]
            imagen.setBackgroundResource(imagenesPorNombre[nombres.get(posicion)] ?: R.drawable.bloqueado)
            imagen.text=""

            val siguiente:Button = findViewById(R.id.Siguiente)
            siguiente.setOnClickListener{
                if(posicion< nombres.size-1) {
                    posicion += 1
                    nombre.text = nombres.get(posicion)
                    descripcion.text = descripciones.get(posicion)
                    imagen.setBackgroundResource(imagenesPorNombre[nombres.get(posicion)] ?: R.drawable.bloqueado)
                }
            }

            val anterior:Button = findViewById(R.id.Anterior)
            anterior.setOnClickListener{
                if(posicion> 0) {
                    posicion -= 1
                    nombre.text = nombres.get(posicion)
                    descripcion.text = descripciones.get(posicion)
                    imagen.setBackgroundResource(imagenesPorNombre[nombres.get(posicion)] ?: R.drawable.bloqueado)
                }
            }

        } else {
            // Manejo del caso en que las listas no existan
            Toast.makeText(this, "No se pudieron recuperar las listas", Toast.LENGTH_SHORT).show()
        }
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                val intent = Intent(this@Reliquias, Laboratorio::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }

}