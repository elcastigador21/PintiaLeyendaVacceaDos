package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameStats : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_stats)
        val intent=intent
        //shows the stats of the game like no. of games played ,won, lost etc.
        val played: TextView = findViewById(R.id.played)
        val won: TextView = findViewById(R.id.won)
        val lost: TextView = findViewById(R.id.lost)
        val bestTime: TextView = findViewById(R.id.bestTime)
        val lastTime: TextView = findViewById(R.id.lastTime)
        val returnCall: Button = findViewById(R.id.return_call)
        with(intent)
        {
            played.text= String.format("played",0)
            won.text = String.format("won, 0")
            lost.text = String.format("lost", 0)
            val best=this.getIntExtra("bestTime",Int.MAX_VALUE)
            bestTime.text = if(best==Int.MAX_VALUE)"NA";else ""+((best / 1000) / 60)+" m "+((best/ 1000) % 60)+" s"
            val last=this.getIntExtra("lastTime",0)
            lastTime.text = if(last==Int.MAX_VALUE)"NA";else ""+((last / 1000) / 60)+" m "+((last/ 1000) % 60)+" s"

        }


// returns back to main screen once user clicks
        returnCall.setOnClickListener {
            val intents= Intent(this, MainActivity::class.java)
            startActivity(intents)
        }
    }
}