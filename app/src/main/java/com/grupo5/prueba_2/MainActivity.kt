package com.grupo5.prueba_2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //make custom directs user to the activity where user can input his own dimensions of the board
        val makeCustom : Button = findViewById(R.id.Laboratorio)
        makeCustom.setOnClickListener{
            val intent = Intent(this@MainActivity, MakeCustomBoard::class.java)
            startActivity(intent)
        }

        val mapa : ConstraintLayout = findViewById(R.id.mapa)
        mapa.setOnClickListener{
            val intent = Intent(this@MainActivity, gameWon::class.java)
            startActivity(intent)
        }
        /*var level=-1
        //self indicates the board to be created itself by the system as per the difficulty chosen by user
        val boardType= "self"
        val easy : RadioButton = findViewById(R.id.easy)
        easy.setOnClickListener{
            level=0
        }
        val medium : RadioButton = findViewById(R.id.medium)
        medium.setOnClickListener{
            level=1
        }
        val difficult : RadioButton = findViewById(R.id.difficult)
        difficult.setOnClickListener{
            level=2
        }
        // start game will take the user to the game screen but will show an alert dialog ton the user if no difficulty is chosen,instructing the user to do so
        val startGame : Button = findViewById(R.id.Mapa)
        startGame.setOnClickListener {
            if(level==-1) {
                val builder= AlertDialog.Builder(this)
                with(builder)
                {
                    setTitle(getString(R.string.difficulty_message))
                    setMessage(getString(R.string.select_difficulty_message))
                    setCancelable(false)
                    setPositiveButton("Ok"
                    ) { dialog, which -> }
                }
                val dialog= builder.create()
                dialog.show()

            } else {
                val intent= Intent(this, MinesweeperGame::class.java).apply {
                    putExtra("Level",level)
                    putExtra("boardType", boardType)
                }
                startActivity(intent)
            }
        }
        // on clicking stats user will be directed to stats page where all the stats regarding game are visible
        val stats : Button = findViewById(R.id.stats)
        stats.setOnClickListener {
    // these shared preferences are directed to main activity by the mine sweeper game activity
            sharedPreferences=
                this.getSharedPreferences("Stats",Context.MODE_PRIVATE)

            val intent = Intent(this, GameStats::class.java).apply {
                with(sharedPreferences) {
                    putExtra("bestTime",  getInt("best",0))
                    putExtra("lastTime",getInt("last",0))
                    putExtra("played", getInt("total",0))
                    putExtra("won",getInt("won",0))
                    putExtra("lost",getInt("lost",0))
                }

            }

            startActivity(intent)
        }*/


    }
}