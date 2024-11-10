package com.grupo5.prueba_2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
        //Ver si se puede poner una lambda
        tocaParaJugar.startAnimation(animacionParpadeo)

    }
}