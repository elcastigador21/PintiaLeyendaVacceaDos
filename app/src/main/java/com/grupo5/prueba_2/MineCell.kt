package com.grupo5.prueba_2

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MineCell @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {


    var value :Int=0
    var isRevealed:Boolean=false
    var isFlagged=false
    var isMine=false
    var isEscalera=false



}