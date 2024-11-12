package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class gameWon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game_won)

        val intent=intent
        val result=intent.getStringExtra("result")

        val trophy: ImageView = findViewById(R.id.trophy)
        val congrats: TextView = findViewById(R.id.congrats)
        val game_message: TextView = findViewById(R.id.game_message)
        val continue_play: Button = findViewById(R.id.continue_play)

        if(result=="Lose")

        {
            trophy.setImageResource(R.drawable.lost)
            congrats.text=getString(R.string.lost_message)
            game_message.text= getString(R.string.game_loss)


        }
        else if(result=="Win")
        {
            trophy.setImageResource(R.drawable.trophy)
            congrats.text=getString(R.string.congratulations)
            game_message.text= getString(R.string.win_message)

        }
        continue_play?.setOnClickListener {
            val intents= Intent(this@gameWon, MainActivity::class.java)
            startActivity(intents)
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                val intent = Intent(this@gameWon, SelectWorld::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }


}