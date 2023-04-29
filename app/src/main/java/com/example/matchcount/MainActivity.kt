package com.example.matchcount

import android.content.Context
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.matchcount.MainActivity.Companion.goals
import com.example.matchcount.MainActivity.Companion.outOfBounces
import com.example.matchcount.MainActivity.Companion.possibleGoals
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        public val context = this
        val goals: ArrayList<Int>  = ArrayList()
        val possibleGoals: ArrayList<Int>  = ArrayList()
        val outOfBounces: ArrayList<Int>  = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView_Test: TextView = findViewById(R.id.textView_test)

        val goals: ArrayList<Int>  = ArrayList()
        val possibleGoal: ArrayList<Int>  = ArrayList()
        val outOfBounces: ArrayList<Int>  = ArrayList()
        goals.add(73)
        goals.add(7)
        goals.add(13)
        overrideStorage(this, goals)
        textView_Test.text=goals.toString()
    }
}
private fun overrideStorage(context: Context, text: ArrayList<Int>){
    // Schreiben in eine Textdatei
    val file = File(context.filesDir, "matchCount.txt")
    file.writeText(text.toString())
}
private fun readStorage(context: Context) {
    val file = File(context.filesDir, "matchCount.txt")
    var data = file.readText()
}
private fun extractData(input: String) {                                                            //make sure, that the lists are int only
    var arrayIndex: Int = 0
    var buffer: String =""
    for (i in input.indices){
        val char: Char = input[i.toInt()]
        if(char=='[') {
            break
        } else if(char==']') {
            when(arrayIndex) {
                1 -> goals.add(buffer.toInt())
                2 -> possibleGoals.add(buffer.toInt())
                3 -> outOfBounces.add(buffer.toInt())
            }
            buffer = ""
            arrayIndex++
        } else if(char==',') {
            buffer+=char.toString()
        } else {
            buffer+=char.toString()
        }
    }
}