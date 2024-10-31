package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MakeCustomBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_custom_board)
        val custom_board_info: ImageButton = findViewById(R.id.custom_board_info)
        custom_board_info.setOnClickListener{
            val builder= AlertDialog.Builder(this)
            with(builder)
            {
                setTitle("Instructions")
                setMessage(
                    """Hi!
 To set up Your own custom Board, You need to provide the dimensions of your board,(preferably in the range of 5-20).
Also you need to provide the number of mines you wish to have in your mines board.
 Hope the Instructions are clear.
Enjoy the Game!! Good Luck!"""
                )
                setCancelable(false)
                setPositiveButton("Ok"
                ) { dialog, which -> }
            }
            val dialog= builder.create()
            dialog.show()
        }

        val start_custom: Button = findViewById(R.id.start_custom)
        val rowsText: EditText = findViewById(R.id.rows)
        val columnsText: EditText = findViewById(R.id.columns)
        val minesText: EditText = findViewById(R.id.mines)
        start_custom.setOnClickListener {
            // if the dimensions are not provided, toast will appear
            if(rowsText.text.isBlank()||columnsText.text.isBlank()||minesText.text.isBlank())
            {
                Toast.makeText(this, "Details of Custom Board not provided",Toast.LENGTH_SHORT). show()
            }
            else
            {
                val rows= Integer.parseInt(rowsText.text.toString())
                val columns= Integer.parseInt(columnsText.text.toString())
                val mines= Integer.parseInt(minesText.text.toString())
                val intent= Intent(this,MinesweeperGame::class.java).apply {
                    putExtra("rows",rows)
                    putExtra("columns",columns)
                    putExtra("mines",mines)
                    putExtra("boardType", "custom")
                }
                startActivity(intent)

            }
        }
    }
}