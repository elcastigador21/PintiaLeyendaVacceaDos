package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SelectWorld : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_world)

        var mundo : Int

        val facil : Button = findViewById(R.id.mundo1)
        facil.setOnClickListener{
            mundo = 0
            val intent = Intent(this, SelectLevel::class.java).apply {
                putExtra("Mundo", mundo) //Mundo 1
            }
            startActivity(intent)
            finish()
        }


        val medio : Button = findViewById(R.id.mundo2)
        medio.setOnClickListener{
            mundo = 1
            val intent = Intent(this, SelectLevel::class.java).apply {
                putExtra("Mundo", mundo) //Mundo 2
            }
            startActivity(intent)
            finish()
        }


        val dificil : Button = findViewById(R.id.mundo3)
        dificil.setOnClickListener{
            mundo = 2
            val intent = Intent(this, SelectLevel::class.java).apply {
                putExtra("Mundo", mundo) //Mundo 3
            }
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                val intent = Intent(this@SelectWorld, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }
}