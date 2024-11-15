package com.grupo5.prueba_2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val laboratorio : Button = findViewById(R.id.Laboratorio)
        laboratorio.setOnClickListener{
            val intent = Intent(this@MainActivity, MakeCustomBoard::class.java)
            startActivity(intent)
        }

        val pantallaInicial : ConstraintLayout = findViewById(R.id.pantallaInicial)
        pantallaInicial.setOnClickListener{
            val intent = Intent(this, SelectWorld::class.java)
            startActivity(intent)
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
    }
}