package com.grupo5.prueba_2.persistence

import java.io.File
import java.io.FileWriter

class DataSaver {

    val filePath = "GameData.txt"
    lateinit var data: GameData

    init {
        load();
    }

    fun load() {
        try {
            val file = File(filePath)

            if (file.exists()) {
                // existe archivo de guardado

                val content = file.readText();
                val list: List<String> = content.split(" ")

                data = GameData(list);
            } else {
                // no existe archivo de guardado

                data = GameData(listOf("0", "0", "0", "0", "0", "0"))
            }

        } catch (e: Exception) {
            println("An error occurred while reading the file: ${e.message}")
        }
    }

    fun save() {
        try {
            val file = File(filePath)
            val writer = FileWriter(file)

            // escribir en el fichero
            writer.write("${data.easyWin} ${data.mediumWin} ${data.hardWin} ${data.easyRelic} ${data.mediumRelic} ${data.hardRelic}")

            writer.close()

        } catch (e: Exception) {
            println("An error occurred while reading the file: ${e.message}")
        }
    }
}