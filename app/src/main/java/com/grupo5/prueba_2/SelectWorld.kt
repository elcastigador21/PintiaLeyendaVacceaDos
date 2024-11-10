package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SelectWorld : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_world)

        val facil : Button = findViewById(R.id.mundo1)
        facil.setOnClickListener{
            val intent = Intent(this, SelectLevel::class.java).apply {
                putExtra("Mundo", 0) //Mundo 1
            }
            startActivity(intent)
        }


        val medio : Button = findViewById(R.id.mundo2)
        medio.setOnClickListener{
            val intent = Intent(this, SelectLevel::class.java).apply {
                putExtra("Mundo", 1) //Mundo 2
            }
            startActivity(intent)
        }


        val dificil : Button = findViewById(R.id.mundo3)
        dificil.setOnClickListener{
            val intent = Intent(this, SelectLevel::class.java).apply {
                putExtra("Mundo", 2) //Mundo 3
            }
            startActivity(intent)
        }
    }
}