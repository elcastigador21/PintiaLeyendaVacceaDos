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

        val imagenesPorNombre = mapOf(
            "Broche de perro" to R.drawable.broche_de_perro,
            "Pomo de puñal" to R.drawable.pomo_de_punal,
            "Cajitas de barro" to R.drawable.cajitas_de_barro,
            "Sonajas" to R.drawable.sonajas,
            "Tintinabulas" to R.drawable.tintinabulas,
            "Estela de clunia" to R.drawable.estela_de_clunia,
            "Denario de Baskunes" to R.drawable.denario_de_baskunes,
            "Tapa de horno" to R.drawable.tapa_de_horno,
            "Zoomorfo vacceo" to R.drawable.zoomorfo_vacceo
        )


        if (nombres != null && descripciones != null) {
            nombre.text = nombres.get(posicion)
            descripcion.text = "El zoomorfo vacceo es una representación simbólica y artística de un animal que forma parte de la cultura de los vacceos, un antiguo pueblo prerromano que habitó la meseta norte de la península ibérica en lo que hoy corresponde a las provincias de Valladolid, Palencia, Zamora, Salamanca y Segovia, entre otros lugares. Estas figuras se realizaban en distintos materiales como piedra, bronce o cerámica y podían tener diversos tamaños, desde pequeñas piezas portátiles hasta grandes esculturas. Los zoomorfos vacceos estaban cargados de significados rituales y espirituales, ya que los vacceos eran una sociedad agraria profundamente conectada con la naturaleza y sus ciclos, atribuyendo a los animales poderes simbólicos y mágicos. Por ejemplo, un toro representado en una escultura podría simbolizar fuerza, fertilidad y protección, reflejando la importancia de este animal en su economía basada en la agricultura y la ganadería. Las formas estilizadas o geométricas de estos animales denotan una clara influencia de las tradiciones artísticas celtas, combinando simplicidad y simbolismo en sus diseños. Los contornos pueden ser toscos o refinados dependiendo de la técnica y el propósito de la obra, pero siempre destacan por resaltar las características esenciales del animal, como los cuernos en el caso de un toro o las garras en el caso de un ave rapaz. Además, estas figuras no solo eran ornamentales, sino que se les atribuía un papel activo en los rituales, sirviendo como amuletos, ofrendas votivas o elementos protectores en tumbas y hogares. En algunos casos, los zoomorfos vacceos podían estar asociados a divinidades o a prácticas de culto relacionadas con la fertilidad de la tierra, el éxito en la caza o la protección del clan. Su diseño no se limitaba a la representación literal, ya que a menudo se combinaban elementos de diferentes animales para crear figuras híbridas que acentuaban su carácter místico, fusionando por ejemplo la fuerza de un toro con la agilidad de un lobo. El hecho de que estos objetos hayan perdurado hasta nuestros días demuestra no solo su durabilidad material, sino también la importancia que tuvieron en la vida cotidiana y espiritual de este pueblo, proporcionando valiosas pistas sobre sus creencias y su visión del mundo."
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