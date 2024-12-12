package com.grupo5.prueba_2

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
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
        val gameMessage: TextView = findViewById(R.id.game_message)
        val continuePlay: Button = findViewById(R.id.continue_play)
        var afd:AssetFileDescriptor = assets.openFd("Goodresult.mp3")


        if(result=="Lose")
        {
            trophy.setImageResource(R.drawable.lost)
            gameMessage.text= getString(R.string.game_loss)
            assets.openFd("Violinlose5.mp3")

        }
        else if(result=="Win")
        {
            assets.openFd("Goodresult.mp3")
            trophy.setImageResource(R.drawable.trophy)
            gameMessage.text= getString(R.string.win_message)

        }

        //Codigo para iniciar la musica de final de nivel cuando se ha ganado
        val mediaPlayer:MediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.prepare()
        mediaPlayer.start()

        continuePlay.setOnClickListener {
            //Codigo para parar la musica
            mediaPlayer.stop();
            mediaPlayer.release();
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