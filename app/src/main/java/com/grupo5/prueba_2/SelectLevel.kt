package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SelectLevel : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_level)

        val mundo = intent.getIntExtra("Mundo", 0)
        var nivel : Int

        //Actualmente no habrá ninguna diferencia entre niveles,
        //pues solo hace caso al metadato Mundo

        val facil : Button = findViewById(R.id.ejemploBoton1)
        facil.setOnClickListener{
            nivel = 0
            val intent = Intent(this, MinesweeperGame::class.java).apply {
                putExtra("Mundo", mundo)
                putExtra("Nivel", nivel)
            }
            startActivity(intent)
            finish()
        }


        val medio : Button = findViewById(R.id.ejemploBoton2)
        medio.setOnClickListener{
            nivel = 1
            val intent = Intent(this, MinesweeperGame::class.java).apply {
                putExtra("Mundo", mundo)
                putExtra("Nivel", nivel)
            }
            startActivity(intent)
            finish()
        }


        val dificil : Button = findViewById(R.id.ejemploBoton3)
        dificil.setOnClickListener{
            nivel = 2
            val intent = Intent(this, MinesweeperGame::class.java).apply {
                putExtra("Mundo", mundo)
                putExtra("Nivel", nivel)
            }
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
               val intent = Intent(this@SelectLevel, SelectWorld::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }
}