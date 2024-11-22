package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class GameWon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game_won)

        val intent=intent
        val result=intent.getStringExtra("result")

        val trophy: ImageView = findViewById(R.id.trophy)
        val congrats: TextView = findViewById(R.id.congrats)
        val gameMessage: TextView = findViewById(R.id.game_message)
        val continuePlay: Button = findViewById(R.id.continue_play)

        if(result=="Lose")

        {
            trophy.setImageResource(R.drawable.lost)
            congrats.text=getString(R.string.lost_message)
            gameMessage.text= getString(R.string.game_loss)


        }
        else if(result=="Win")
        {
            trophy.setImageResource(R.drawable.trophy)
            congrats.text=getString(R.string.congratulations)
            gameMessage.text= getString(R.string.win_message)

        }
        continuePlay.setOnClickListener {
            val intents= Intent(this@GameWon, MainActivity::class.java)
            startActivity(intents)
            finish()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                val intents = Intent(this@GameWon, SelectWorld::class.java)
                startActivity(intents)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }


}