package com.grupo5.prueba_2

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var jsonArray: JSONArray
    private val outputFileName = "reliquias_modificadas.json"
    private val inputFileName = "json/reliquias.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val laboratorio : Button = findViewById(R.id.Laboratorio)
        laboratorio.setOnClickListener{
            val intent = Intent(this@MainActivity, Laboratorio::class.java)
            startActivity(intent)
            finish()
        }

        val pantallaInicial : ConstraintLayout = findViewById(R.id.pantallaInicial)
        pantallaInicial.setOnClickListener{
            val intent = Intent(this, SelectWorld::class.java)
            startActivity(intent)
            finish()
        }

        val tocaParaJugar : TextView = findViewById(R.id.toca_para_jugar)
        //Animacion de parpadeo del texto
        val animacionParpadeo = AlphaAnimation(0.0f, 1.0f).apply{
            duration = 500
            startOffset = 20
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }
        tocaParaJugar.startAnimation(animacionParpadeo)

        val web : ImageButton = findViewById(R.id.web)
        web.setOnClickListener{
            val url = getString(R.string.web)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        val facebook : ImageButton = findViewById(R.id.facebook)
        facebook.setOnClickListener{
            val url = getString(R.string.facebook)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        val twitter : ImageButton = findViewById(R.id.twitter)
        twitter.setOnClickListener{
            val url = getString(R.string.twitter)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        val outputFile = File(this@MainActivity.filesDir, outputFileName)

        if(!outputFile.exists()) {
            copiarJson(this)
        }

        val dialogoMenuBoton : ImageButton = findViewById(R.id.configuracion)
        val dialogo = Dialog(this@MainActivity)

        dialogoMenuBoton.setOnClickListener{
            dialogo.setContentView(R.layout.dialogo_menu)
            dialogo.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialogo.setCancelable(false)
            dialogo.window?.attributes?.windowAnimations = R.style.animacion_dialogo

            val cancelar : ImageButton = dialogo.findViewById(R.id.cancelar)
            cancelar.setOnClickListener {
                dialogo.dismiss()
            }

            val resetear: Button = dialogo.findViewById(R.id.resetear)
            resetear.setOnClickListener{
                val builder= AlertDialog.Builder(this)
                with(builder) {

                    setTitle("Resetear datos")
                    setMessage("¿Seguro que quieres borrar tu progreso en el juego? \n (Esta acción no se puede deshacer)")
                    setPositiveButton("Si")
                    { dialog, which ->
                        copiarJson(this@MainActivity)
                        dialogo.dismiss()
                        Toast.makeText(this@MainActivity, "Has reseteado el progreso", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No")
                    { dialog, which ->  }
                }
                val alert=builder.create()
                alert.show()
            }
            dialogo.show()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }


    // Copiar archivo JSON desde assets a filesDir
    private fun copiarJson(context: MainActivity) {
        val outputFile = File(context.filesDir, outputFileName)
        try {
            context.assets.open(inputFileName).use { inputStream ->
                outputFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            val dialogo = AlertDialog.Builder(context)
            dialogo.apply {
                setTitle("Error al copiar el archivo")
                setMessage(e.toString())
                setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            }.create().show()
        }

    }

}