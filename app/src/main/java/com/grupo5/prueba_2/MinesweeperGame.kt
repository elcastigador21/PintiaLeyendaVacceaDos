package com.grupo5.prueba_2

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.random.Random

class MinesweeperGame : AppCompatActivity() {

    private var flaggedmines=0

    private var isFirstmove=true
    private var status= Status.ONGOING
    private var rows=0
    private var columns=0
    private var romanos=0
    private var mineboard= Array(rows){Array(columns){MineCell(this)}}
    private var playStarted=false
    private var played=0
    private var won=0
    private var lost=0
    private var lastGameTime=Integer.MAX_VALUE
    private var fastestTime=Integer.MAX_VALUE
    private var level = -1

    private var personajeColocado = false
    private var personajeX = -1
    private var personajeY = -1
    private var cuentaAtras : CountDownTimer? = null

    //TODO: Poner variable que controle las reliquias

    companion object {
        const val ROMANO = -1
        const val ESCALERA = -2
        const val PERSONAJE = -3
        const val TIEMPO_FACIL : Long = 400_000     //
        const val TIEMPO_MEDIO : Long = 600_000     // Faltan de ajustar
        const val TIEMPO_DIFICIL : Long = 700_000   //
        const val reveal="reveal"
        const val flag= "flag"
        val movement= arrayOf(-1,0,1)
    }

    private var toMove: String =reveal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minesweeper_game)
        //starts the timer
        if(!playStarted) {
            Toast.makeText(this,"Toca el tablero para comenzar. ¡Buena suerte!",Toast.LENGTH_LONG).show()
            playStarted
        }
        val intent=intent
        //level indicates the difficulty level of game
        level=intent.getIntExtra("Level",1)

        when (level) {
            //TODO: Incluir el numero de trozos de reliquias
            0 -> {
                rows=8
                columns=8
                romanos= 10
                establecerCuentaAtras(TIEMPO_FACIL)
            }
            1 -> {
                rows=12
                columns=12
                romanos= 25
                establecerCuentaAtras(TIEMPO_MEDIO)
            }
            2 -> {
                rows=16
                columns=16
                romanos=40
                establecerCuentaAtras(TIEMPO_DIFICIL)
            }
        }


        //mines left will show the number of mines not flagged
        val minesLeftinfo : TextView = findViewById(R.id.minesLeftinfo)
        val instructions : Button = findViewById(R.id.instructions)
        val restart : Button = findViewById(R.id.restart)
        minesLeftinfo.text= romanos.toString()
        //Se crea un tablero con las dimensiones del nivel de dificultad escogido
        createBoard()
        //Cuando el usuario toca el icono de instrucciones se muestra un cuadro de dialogo con las instrucciones del juego
        instructions.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            with(builder) {

                //TODO:Cambiar mensaje

                setTitle("Instrucciones del juego")
                setMessage(
                    "Hi! Welcome to the Minesweeper Game." +
                            "Here, you are provided with a board having certain no. of mines hidden in it."+
                            "You can start your game by clicking on a random block." +
                            "You can either open a block or flag it." +
                            "Either a block has a number, or a mine hidden in it , otherwise it is empty." +
                            "The number on a block depicts the no. of mines that surround it." +
                            "The purpose is to open all such blocks with no mines hidden in them." +
                            "You may flag all those blocks where mines are hidden." +
                            "Enjoy the game!!" +
                            "Good Luck!!"
                )
                setCancelable(false)
                setPositiveButton("Ok"
                ) { dialog, which -> }

            }

            val instructions= builder.create()
            instructions.show()
        }
        // on clicking restart icon,ser will be given an alert dialog confirming the action and if clicked yes, game will restart again

        restart.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            with(builder) {

                setTitle("Reiniciar juego")
                setMessage("La partida esta en curso. ¿Seguro que quieres reiniciar?")
                setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
                    val intents= getIntent()
                    finish()
                    startActivity(intents)
                })
                setNegativeButton("No"

                ) { dialog, which ->  }
            }
            val alert=builder.create()
            alert.show()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                ponerDialogoAtras()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }


    private fun createBoard() {
        mineboard= Array(rows){Array(columns){MineCell(this)}}
        val params1= LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)

        val params2= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)

        val choice : ImageButton = findViewById(R.id.choice)
        var id=1
        //on clicking choice button user can switch its moves between reveal and flag
        choice.setOnClickListener {

            if(toMove== reveal) {
                toMove= flag
                choice.setImageResource(R.drawable.flag)

            }
            else {
                toMove= reveal
                choice.setImageResource(R.drawable.pala)
            }
        }

        for(i in 0 until rows) {
            val linearLayout= LinearLayout(this)
            linearLayout.orientation= LinearLayout.HORIZONTAL
            linearLayout.layoutParams=params2
            params2.weight=1.0F

            //  params2.bottomMargin= 5
            for(j in 0 until columns) {
                val button= MineCell(this)
                mineboard[i][j]=button
                button.id=id

                id++
                button.layoutParams=params1
                params1.weight=1.0F
                button.setBackgroundResource(R.drawable.ladrillo)
                // params1.rightMargin=5

                linearLayout.addView(button)
                button.setOnClickListener {
                    // if the user has clicked the cell first time , the mines will be setup in the game ensuring the first clicked cell isnt a mine/bomb.
                    if (isFirstmove) {
                        setUpMines(i, j)
                        //TODO: Poner las reliquias en el tablero
                    }

                    move(toMove, i, j)

                    if(isFirstmove){
                        isFirstmove = false
                        ponerEscalera()
                    }
                    displayBoard()
                    if(isComplete() && status!=Status.LOST) {
                        status = Status.WON
                        finalResult()
                    }
                }
            }
            val board : LinearLayout = findViewById(R.id.board)
            board.addView(linearLayout)
        }

    }

    //used to display the cells
    private fun displayBoard() {
        mineboard.forEach { row ->
            row.forEach {
                if(it.isRevealed)
                    setImage(it)
                else if (it.isFlagged) {

                    if (status == Status.LOST && !it.isMine())
                        it.setBackgroundResource(R.drawable.crossedflag)
                    else
                        it.setBackgroundResource(R.drawable.flag)
                }

                //else if (status == Status.LOST && it.value == MINE)
                    //it.setBackgroundResource(R.drawable.romano)
                //else if (status == Status.WON && it.value == MINE)
                    //it.setBackgroundResource(R.drawable.flag)
                else
                    it.text=" "
            }

        }
    }

    private fun setImage(minecell: MineCell) {

        with(minecell) {
            when (value) {
                -3 -> setBackgroundResource(R.drawable.protagonista)
                -2 -> setBackgroundResource(R.drawable.escalera)
                -1 -> setBackgroundResource(R.drawable.romano)
                0 -> setBackgroundResource(R.drawable.opened)
                1 -> setBackgroundResource(R.drawable.one)
                2 -> setBackgroundResource(R.drawable.two)
                3 -> setBackgroundResource(R.drawable.three)
                4 -> setBackgroundResource(R.drawable.four)
                5 -> setBackgroundResource(R.drawable.five)
                6 -> setBackgroundResource(R.drawable.six)
                7 -> setBackgroundResource(R.drawable.seven)
                8 -> setBackgroundResource(R.drawable.eight)
            }
        }
    }


    //Revela o marca una celda dependiendo de la opcion elegida por el usuario
    private fun move(choice:String, x: Int, y:Int){

        when(choice){
            reveal -> {
                modoRevelar(x,y)
            }
            flag -> {
                modoMarcar(x,y)
            }
        }
        return
    }

    private fun modoRevelar(x : Int, y : Int)  {

        //TODO: que pasa si se toca una reliquia

        //Si es el primer movimiento se coloca al personaje
        if (!personajeColocado) {
            mineboard[x][y].value = PERSONAJE
            personajeX = x
            personajeY = y
            personajeColocado = true
            reveal(x, y)
            cuentaAtras?.start()
            return
        }

        //El personaje se podra mover libremente por las casillas ya reveladas
        if (mineboard[x][y].isRevealed) {

            //Si selecciona la escalera ganara la partida
            if (mineboard[x][y].value == ESCALERA) {
                status = Status.WON
                finalResult()

            } else if (mineboard[x][y].value >= 0) {
                mineboard[x][y].valorOriginal = mineboard[x][y].value
                mineboard[personajeX][personajeY].value =
                    mineboard[personajeX][personajeY].valorOriginal

                personajeX = x
                personajeY = y

                mineboard[x][y].value = PERSONAJE
                reveal(x, y)

            } else if (mineboard[x][y].value == ROMANO) {
                val cuentaAtrasText: TextView = findViewById(R.id.cuentaAtrasText)
                reducirTiempo(cuentaAtrasText)
            }

        //Si la casilla aun no ha sido revelada, solo podra moverse de una en una
        // (tambien en diagonal) y esta sera revelada
        } else {
            if ((Math.abs(personajeX - x) <= 1 && Math.abs(personajeY - y) <= 1)) {

                //Si toca una mina "perdera"/"penalizara" la partida
                if (mineboard[x][y].value == ROMANO) {
                    val cuentaAtrasText: TextView = findViewById(R.id.cuentaAtrasText)
                    reducirTiempo(cuentaAtrasText)

                } else if (mineboard[x][y].value >= 0 && !mineboard[x][y].isFlagged) {
                    mineboard[x][y].valorOriginal = mineboard[x][y].value //Destino

                    mineboard[personajeX][personajeY].value =
                        mineboard[personajeX][personajeY].valorOriginal    //Origen


                    personajeX = x
                    personajeY = y

                    mineboard[x][y].value = PERSONAJE

                }
                reveal(x,y)
            }
        }
        return
    }

    private fun modoMarcar(x : Int, y : Int ) {
        val minesLeftinfo : TextView = findViewById(R.id.minesLeftinfo)

        with (mineboard[x][y]) {
            if(isRevealed) {
                return //ESTO ES NECESARIO, NO QUITAR
            }

            if(!isFlagged && flaggedmines==romanos) {
                Toast.makeText(this@MinesweeperGame,"You cannot flag more mines",Toast.LENGTH_SHORT).show()
                return
            }

            if(isFlagged) {
                setBackgroundResource(R.drawable.ladrillo)
                flaggedmines--
            }
            else {
                setBackgroundResource(R.drawable.flag)
                flaggedmines++
            }
            isFlagged = !isFlagged
            minesLeftinfo.text= (romanos-flaggedmines).toString()

            return
        }
    }



    private fun finalResult() {
        //timer stops as soon as game finishes
        cuentaAtras?.cancel()

        //TODO: Tiene que escribir en el json

        val elapsedTime = 0
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val currScore = elapsedTime.toInt()


        fastestTime= sharedPref.getInt("BestScore", Integer.MAX_VALUE)
        lastGameTime = sharedPref.getInt("LastTime", Integer.MAX_VALUE)
        played= sharedPref.getInt("Played",0)
        won= sharedPref.getInt("Won",0)
        lost= sharedPref.getInt("Lost",0)
        sharedPref.edit(). putInt("Played",played+1).apply()
        played++

        if(status==Status.WON) {
            lastGameTime=currScore
            won++
            if(currScore<fastestTime) {
                fastestTime = currScore
            }
        }

        else if(status == Status.LOST){
            lost++
            lastGameTime=Int.MAX_VALUE
        }// updation is done as per the result of game
        with(sharedPref.edit()) {
            putInt("BestScore",fastestTime)
            putInt("LastTime",lastGameTime)
            putInt("Won",won)
            putInt("Lost",lost)

            apply()
        }

        // shared preferences used to store and share game stats with the main activity
        val sharedPreferences :SharedPreferences=this.getSharedPreferences("Stats",Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("total",played)
            putInt("won",won)
            putInt("last",lastGameTime)
            putInt("best",fastestTime)
            putInt("lost",lost)
            apply()

        }

        if(status==Status.WON) {
            val intent= Intent(this, gameWon::class.java).apply {
                putExtra("result","Won")
            }
            startActivity(intent)
        }

        else if(status==Status.LOST) {
            val intent= Intent(this, gameWon::class.java).apply {
                putExtra("result","Lose")
            }
            startActivity(intent)
        }
    }


    // isComplete() function checks for the win conditions
    private fun isComplete():Boolean{
        mineboard.forEach{ row->
            row.forEach{

                if(it.value == ROMANO) {
                    if(!it.isFlagged && !it.isRevealed) {
                        return false
                    }
                }
                else{
                    if(!it.isRevealed) {
                        return false
                    }
                }

            }
        }

        status=Status.WON
        return true
    }

    //Ahora tambien revela a los romanos
    private fun reveal(x: Int,y:Int) {
        if(!mineboard[x][y].isRevealed && !mineboard[x][y].isFlagged) {
            mineboard[x][y].isRevealed=true
            if(mineboard[x][y].value==0) {
                for(i in movement)
                    for(j in movement)
                        if((i!=0 || j!=0) && ((x+i) in 0 until rows ) && ((y+j) in 0 until columns))
                            reveal(x+i,y+j)
            }
        }
    }


    //Cuadro de dialogo que se muestra cuando el usuario pulsa el boton atras del telefono
    private fun ponerDialogoAtras(){
        val dialogo = AlertDialog.Builder(this)
        dialogo.apply{
            setTitle(getString(R.string.exit_game))
            setMessage(getString(R.string.abort))
            setPositiveButton("Si") { dialog, which ->
                finalResult()
                val intent = Intent(this@MinesweeperGame, SelectLevel::class.java).apply{
                    putExtra("Mundo", level)
                }
                startActivity(intent)
                finish()
            }
            setNegativeButton("No") { dialog, which -> }
        }.create().show()
    }


    //mines are set randomly
    private fun setUpMines(i: Int, j: Int) {
        var m=1
        while(m<=romanos) {
            val x= Random(System.nanoTime()).nextInt(0,rows)
            val y= Random(System.nanoTime()).nextInt(0,columns)
            if(((x in i-1..i+1)&&(y in j-1..j+1))||mineboard[x][y].isMine()) {
                continue
            }
            else {
                mineboard[x][y].value = -1
            }
            updateNeighbours(x,y)
            m++
        }
    }

    //Pone la escalera de salida del nivel en una celda libre
    private fun ponerEscalera(){
        var coordX : Int
        var coordY : Int

        do{
            coordX = Random(System.nanoTime()).nextInt(0,rows);
            coordY = Random(System.nanoTime()).nextInt(0,columns);

        } while(mineboard[coordX][coordY].isMine() || mineboard[coordX][coordY].getCellValue() != 0)


        mineboard[coordX][coordY].value= ESCALERA;
    }


    //Funcion que implementa la cuenta atras. Si el tiempo se acaba pierdes
    private fun establecerCuentaAtras(tiempo : Long){
        val cuentaAtrasText : TextView = findViewById(R.id.cuentaAtrasText)
        cuentaAtras = object : CountDownTimer(tiempo, 1000){
            override fun onTick(tiempoRestante: Long){
                val minutos : Long = (tiempoRestante/1000) / 60
                val segundos : Long = (tiempoRestante/1000) % 60

                cuentaAtrasText.text = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos)
            }
            override fun onFinish() {
                status = Status.LOST
                finalResult()
            }
        }
    }

    //Reduce el tiempo al tocar un romano
    private fun reducirTiempo(cuentaAtrasText : TextView){
        cuentaAtras?.cancel()

        var tiempo : String = cuentaAtrasText.text.toString()
        var minutos: Long = tiempo.subSequence(0,2).toString().toLong()
        var segundos: Long = tiempo.subSequence(3,5).toString().toLong()

        var milisegundos : Long = ((minutos*60) + segundos)*1000

        milisegundos -= 5000 * (level+1)

        establecerCuentaAtras(milisegundos)

        cuentaAtras?.start()
    }


    private fun updateNeighbours(  x: Int, y: Int) {
        for (i: Int in movement) {
            for (j in movement) {
                if(((x+i) in 0 until rows) && ((y+j) in 0 until columns) && mineboard[x+i][y+j].value >= 0) {
                    mineboard[x+i][y+j].value++
                    mineboard[x+i][y+j].valorOriginal++
                }
            }
        }
    }
    
}

enum class Status{
    WON,
    ONGOING,
    LOST
}