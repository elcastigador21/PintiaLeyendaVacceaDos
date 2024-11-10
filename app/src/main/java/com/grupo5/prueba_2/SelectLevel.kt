package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SelectLevel : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_level)

        val nivel = intent.getIntExtra("Mundo", 0)

        //Actualmente no habrá ninguna diferencia entre niveles,
        // pues solo hace caso al metadato Mundo

        val facil : Button = findViewById(R.id.ejemploBoton1)
        facil.setOnClickListener{
            val intent = Intent(this, MinesweeperGame::class.java).apply {
                putExtra("Level", nivel)
                putExtra("boardType", "self")
            }
            startActivity(intent)
        }


        val medio : Button = findViewById(R.id.ejemploBoton2)
        medio.setOnClickListener{
            val intent = Intent(this, MinesweeperGame::class.java).apply {
                putExtra("Level", nivel)
                putExtra("boardType", "self")
            }
            startActivity(intent)
        }


        val dificil : Button = findViewById(R.id.ejemploBoton3)
        dificil.setOnClickListener{
            val intent = Intent(this, MinesweeperGame::class.java).apply {
                putExtra("Level", nivel)
                putExtra("boardType", "self")
            }
            startActivity(intent)
        }
    }
}