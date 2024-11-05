package com.grupo5.prueba_2

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MineCell @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {


    var value :Int = 0
    var isRevealed:Boolean = false
    var isFlagged = false
    var isMine = false
    var isEscalera = false
    var valorOriginal = 0

    //TODO: geters y seters
    public fun getValue() : Int {
        return value
    }

    public fun getIsRevealed() : Boolean {
        return isRevealed
    }

    public fun getIsFlagged() : Boolean {
        return isFlagged
    }

    public fun getIsMine() : Boolean {
        return isMine
    }

    public fun getIsEscalera() : Boolean {
        return isEscalera
    }

    public fun getValorOriginal() : Int {
        return valorOriginal
    }

}