package com.grupo5.prueba_2

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MineCell @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {


    var value : Int = 0
    var valorOriginal : Int = 0

    var isRevealed : Boolean = false

    var isFlagged : Boolean = false

    //TODO: geters y seters


    public fun getCellValue() : Int {
        return value
    }

    public fun getCellIsRevealed() : Boolean {
        return isRevealed
    }

    public fun getCellIsFlagged() : Boolean {
        return isFlagged
    }

    public fun isEscalera() : Boolean {
        return (getCellValue() == -2)
    }

    public fun getCellValorOriginal() : Int {
        return valorOriginal
    }

    public fun isMine() : Boolean{
        return (getCellValue() == -1)
    }

}