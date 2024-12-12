package com.grupo5.prueba_2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.File

class Laboratorio : AppCompatActivity(){

    private lateinit var jsonArray: JSONArray
    private val outputFileName = "reliquias_modificadas.json"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboratorio)

        val reliquias : ListView = findViewById(R.id.reliquias)
        val nombres = mutableListOf<String>()
        val descripciones = mutableListOf<String>()

        // Leer el JSON desde filesDir y convertirlo en una lista
        val lista = leerJson(outputFileName)

        for(elemento in lista){
            val partes = elemento.split("-")
            if(partes.size==2) {
                nombres.add(partes[0])
                descripciones.add(partes[1])
            }
            else{
                nombres.add(partes[0])
                descripciones.add("???")
            }
        }

        // Crear el adaptador y asociarlo con el ListView
        val adapter = ArrayAdapter(this, R.layout.elemento_lista, nombres)
        reliquias.adapter = adapter

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged()

        // Configurar acción al hacer clic en un elemento
        reliquias.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@Laboratorio, Reliquias::class.java)
            // Pasar las listas y la posición
            intent.putStringArrayListExtra("nombres", ArrayList(nombres))
            intent.putStringArrayListExtra("descripciones", ArrayList(descripciones))
            intent.putExtra("posicion", position)

            // Iniciar la nueva actividad
            startActivity(intent)
            finish()
        }
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                val intent = Intent(this@Laboratorio, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

    }

    // Leer el archivo JSON desde filesDir y convertirlo en una lista
    private fun leerJson(outputFileName: String): List<String> {
        val palabras = mutableListOf<String>()
        try {
            val file = File(filesDir, outputFileName)

            // Leer el contenido del archivo
            val jsonString = file.bufferedReader().use { it.readText() }

            // Convertir el string JSON en un JSONArray
            jsonArray = JSONArray(jsonString)

            // Obtener la lista de nombres (o ??? si no está descubierta)
            palabras.addAll(obtenerReliquias(jsonArray))
        } catch (e: Exception) {
            val dialogo = AlertDialog.Builder(this)
            dialogo.apply {
                setTitle("Error al leer el archivo")
                setMessage(e.toString())
                setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            }.create().show()
        }
        return palabras
    }


    // Convertir un JSONArray a una lista de nombres para el ListView
    private fun obtenerReliquias(jsonArray: JSONArray): List<String> {
        val palabras = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val objeto = jsonArray.getJSONObject(i)
            val nombre = if (objeto.getBoolean("descubierta")) {
                objeto.getString("nombre")+"-"+objeto.getString("descripcion")
            } else {
                "???"
            }
            print(nombre)
            palabras.add(nombre)
        }
        return palabras
    }

}