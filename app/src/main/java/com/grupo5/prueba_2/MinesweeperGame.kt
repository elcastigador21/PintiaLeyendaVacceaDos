package com.grupo5.prueba_2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.Locale
import kotlin.math.abs
import kotlin.random.Random

class MinesweeperGame : AppCompatActivity() {

    private var flaggedmines=0

    private var isFirstmove=true
    private var status= Status.ONGOING
    private var rows=0
    private var columns=0
    private var romanos=0
    private var fragmentoReliquia=0
    private var mineboard= Array(rows){Array(columns){MineCell(this)}}
    private var playStarted=false
    private var mundo = -1
    private var nivel = -1

    private var personajeColocado = false
    private var personajeX = -1
    private var personajeY = -1
    private var cuentaAtras : CountDownTimer? = null

    private val outputFileName = "reliquias_modificadas.json"

    private val mediaPlayer: MediaPlayer = MediaPlayer()


    companion object {
        const val ROMANO = -1
        const val ESCALERA = -2
        const val PERSONAJE = -3
        const val FRAGMENTO_RELIQUIA = -4
        const val TIEMPO_FACIL : Long = 240_000    // 4 minutos
        const val TIEMPO_MEDIO : Long = 420_000     // 7 minutos
        const val TIEMPO_DIFICIL : Long = 700_000   // 11 y pico
        const val REVEAL="reveal"
        const val FLAG= "flag"
        val movement= arrayOf(-1,0,1)
    }

    private var toMove: String = REVEAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minesweeper_game)

        if(!playStarted) {
            Toast.makeText(this,"Toca el tablero para comenzar. ¡Buena suerte!",Toast.LENGTH_LONG).show()
            playStarted
        }

        val intent=intent
        //Mundo indica la dificultad
        mundo=intent.getIntExtra("Mundo",0)
        nivel=intent.getIntExtra("Nivel", 0)

        //Codigo para iniciar la musica del nivel
        val afd = assets.openFd("Cancion3.mp4")
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.prepare()
        mediaPlayer.start()



        when (mundo) {

            0 -> {
                fragmentoReliquia = 2
                if(nivel != 2){
                    fragmentoReliquia = mundo + 1
                }
                rows=8
                columns=8
                romanos=10
                establecerCuentaAtras(TIEMPO_FACIL)
            }
            1 -> {
                fragmentoReliquia = 3
                if(nivel != 2){
                    fragmentoReliquia = mundo + 1
                }
                rows=12
                columns=12
                romanos=25
                establecerCuentaAtras(TIEMPO_MEDIO)
            }
            2 -> {
                fragmentoReliquia=3

                rows=16
                columns=16
                romanos=40
                establecerCuentaAtras(TIEMPO_DIFICIL)
            }
        }


        //mines left will show the number of mines not flagged
        val minesLeftinfo : TextView = findViewById(R.id.minesLeftinfo)
        val secretosRestantes : TextView = findViewById(R.id.secretosRestantes)
        val instructions : Button = findViewById(R.id.instructions)
        val restart : Button = findViewById(R.id.restart)

        minesLeftinfo.text = String.format("%S", romanos)
        secretosRestantes.text = String.format("%S", fragmentoReliquia)

        //Se crea un tablero con las dimensiones del nivel de dificultad escogido
        createBoard()
        //Cuando el usuario toca el icono de instrucciones se muestra un cuadro de dialogo con las instrucciones del juego
        instructions.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            with(builder) {
                setTitle(getString(R.string.instrucciones))
                setMessage(
                    "¡Bienvenido a Pintia: la Leyenda Vaccea!\n" +
                            "El juego comenzará cuando cliques la primera casilla.\n" +
                            "las casillas pueden ser: desconocidas, marcadas, seguras, romanos, numericas, secretos, o la escalera.\n" +
                            "Las casillas numeradas indican por cuantos romanos están rodeadas.\n" +
                            "Puedes ganar la partida clickando la escalera, o explorando todo el mapa.\n" +
                            "Marca las casillas que contengan romanos.\n" +
                            "Aprovecha el nivel 1-1, para familiarizarte con el juego.\n" +
                            "Recuerda que juegas contra el tiempo. Suerte!!!."
                )
                setCancelable(false)
                setPositiveButton("Ok"
                ) { dialog, which -> }

            }

            val dialogoInstrucciones= builder.create()
            dialogoInstrucciones.show()
        }

        //Al clickar en reiniciar, aparecera un dialogo para confirmar
        restart.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            with(builder) {
                setTitle("Reiniciar juego")
                setMessage("La partida esta en curso. ¿Seguro que quieres reiniciar?")
                setPositiveButton("Si")
                    { dialog, which ->
                        val intents= getIntent()
                        finish()
                        startActivity(intents)
                    }
                setNegativeButton("No")
                { dialog, which ->  }

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

        //Si se hace click en el boton choice, se puede cambiar entre pala y bandera
        choice.setOnClickListener {
            if(toMove== REVEAL) {
                toMove= FLAG
                choice.setImageResource(R.drawable.flag)
            }
            else {
                toMove= REVEAL
                choice.setImageResource(R.drawable.pala)
            }
        }


        // Codigo para repetir la musica del nivel
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0) // Reset to the beginning
            mediaPlayer.start()    // Start playing again
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
                    //Las bombas se ponen cuando el usuario ha realizado el primer movimiento
                    if (isFirstmove) {
                        setUpMines(i, j)
                        ponerFragmentoReliquia()
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

    //Muestra el tablero
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
                -4 -> setBackgroundResource(R.drawable.jarron_roto)
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
            REVEAL -> {
                modoRevelar(x,y)
            }
            FLAG -> {
                modoMarcar(x,y)
            }
        }
        return
    }

    private fun modoRevelar(x : Int, y : Int)  {
        val secretosRestantes : TextView = findViewById(R.id.secretosRestantes)

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

            }
            else if (mineboard[x][y].value >= 0) {
                mineboard[x][y].valorOriginal = mineboard[x][y].value
                mineboard[personajeX][personajeY].value =
                    mineboard[personajeX][personajeY].valorOriginal

                personajeX = x
                personajeY = y

                mineboard[x][y].value = PERSONAJE
                reveal(x, y)

            }
            else if (mineboard[x][y].value == ROMANO){
                mineboard[x][y].valorOriginal = mineboard[x][y].value
                mineboard[personajeX][personajeY].value =
                    mineboard[personajeX][personajeY].valorOriginal

                personajeX = x
                personajeY = y

                mineboard[x][y].value = PERSONAJE

                val cuentaAtrasText: TextView = findViewById(R.id.cuentaAtrasText)
                reducirTiempo(cuentaAtrasText)

            }
            else if(mineboard[x][y].getCellValue() == FRAGMENTO_RELIQUIA){
                mineboard[x][y].value = 0
                fragmentoReliquia --
                secretosRestantes.text = String.format("%S", fragmentoReliquia)
            }

        //Si la casilla aun no ha sido revelada, solo podra moverse de una en una
        // (tambien en diagonal) y esta sera revelada
        }
        else {
            if ((abs(personajeX - x) <= 1 && abs(personajeY - y) <= 1)) {

                //Si toca un romano "penalizara" la partida si no esta marcada
                if (mineboard[x][y].value == ROMANO && !mineboard[x][y].isFlagged) {
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
                Toast.makeText(this@MinesweeperGame,"No puedes marcar mas romanos",Toast.LENGTH_SHORT).show()
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
            minesLeftinfo.text= String.format("%S",(romanos-flaggedmines))

            return
        }
    }



    private fun finalResult() {
        //Para la cuenta atras
        cuentaAtras?.cancel()

        //Codigo para parar la musica
        mediaPlayer.stop();
        mediaPlayer.release();

        if(status==Status.WON) {
            if(fragmentoReliquia == 0){
                descubirReliquia()
            }
            val intent= Intent(this, GameWon::class.java).apply {
                putExtra("result","Won")
            }
            startActivity(intent)
            finish()
        }

        else if(status==Status.LOST) {
            val intent= Intent(this, GameWon::class.java).apply {
                putExtra("result","Lose")
            }
            startActivity(intent)
            finish()
        }
    }

    private fun descubirReliquia(){
        try {
            val jsonArray = leerJson()
            // Obtener el objeto correspondiente
            val objetoEncontrado : JSONObject? = buscarReliquiaPorId(jsonArray, mundo, nivel)


            // Cambiar el estado
            objetoEncontrado?.put("descubierta", true)

            // Guardar los cambios en el archivo
            guardarJsonEnFilesDir(jsonArray)

        } catch (e: Exception) {
            val dialogo = AlertDialog.Builder(this)
            dialogo.apply {
                setTitle("Error")
                setMessage(e.toString())
                setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            }.create().show()
        }
    }

    // Función para buscar la reliquia por idMundo y idNivel
    private fun buscarReliquiaPorId(jsonArray: JSONArray, idMundo: Int, idNivel: Int): JSONObject? {
        for (i in 0 until jsonArray.length()) {
            val objeto = jsonArray.getJSONObject(i)
            if (objeto.getInt("mundoId") == idMundo && objeto.getInt("nivelId") == idNivel) {
                return objeto
            }
        }
        // Devuelve null si no se encuentra ninguna coincidencia
        return null
    }

    // Leer el archivo JSON desde filesDir y convertirlo en una lista
    private fun leerJson(): JSONArray {
        lateinit var jsonArray : JSONArray
        try {
            val file = File(filesDir, outputFileName)

            val jsonString = file.bufferedReader().use { it.readText() }

            jsonArray = JSONArray(jsonString)

        }
        catch (e: Exception) {
            val dialogo = AlertDialog.Builder(this)
            dialogo.apply {
                setTitle("Error al leer el archivo")
                setMessage(e.toString())
                setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            }.create().show()
        }
        return jsonArray
    }

    // Guarda el JSONArray en el archivo JSON
    private fun guardarJsonEnFilesDir(jsonArray: JSONArray) {
        try {
            val file = File(filesDir, outputFileName)
            file.writeText(jsonArray.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    // isComplete verifica las condiciones para ganar la partida
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
                    putExtra("Mundo", mundo)
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

    //Las reliquias se disponene en el tablero, siempre y cuando no haya ni una mina, ni un numero
    private fun ponerFragmentoReliquia(){
        var coordX : Int
        var coordY : Int

        var indice = 0

        while(indice < fragmentoReliquia) {
            do {
                coordX = Random(System.nanoTime()).nextInt(0, rows);
                coordY = Random(System.nanoTime()).nextInt(0, columns);

            } while (mineboard[coordX][coordY].isMine() || mineboard[coordX][coordY].getCellValue() != 0)

            mineboard[coordX][coordY].value = FRAGMENTO_RELIQUIA;
            indice ++
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

        val tiempo : String = cuentaAtrasText.text.toString()
        val minutos: Long = tiempo.subSequence(0,2).toString().toLong()
        val segundos: Long = tiempo.subSequence(3,5).toString().toLong()

        var milisegundos : Long = ((minutos*60) + segundos)*1000

        milisegundos -= 15_000 * (nivel+1) + 10_000 * (mundo)

        establecerCuentaAtras(milisegundos)

        cuentaAtras?.start()
    }


    //Coloca los numeros segun los romanos que haya alrededor
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