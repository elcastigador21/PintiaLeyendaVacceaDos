package com.grupo5.prueba_2.persistence

class GameData(list: List<String>) {
    var easyWin: Int = list[0].toInt();
    var mediumWin: Int = list[1].toInt();
    var hardWin: Int = list[2].toInt();

    var easyRelic: Int = list[3].toInt();
    var mediumRelic: Int = list[4].toInt();
    var hardRelic: Int = list[5].toInt();
}