package com.example.matchcount

import android.content.Context
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.matchcount.MainActivity.Companion.arrayAdapter_control
import com.example.matchcount.MainActivity.Companion.array_control
import com.example.matchcount.MainActivity.Companion.gameNumber
import com.example.matchcount.MainActivity.Companion.goals
import com.example.matchcount.MainActivity.Companion.outOfBounces
import com.example.matchcount.MainActivity.Companion.possibleGoals
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        val context = this
        var goals:          ArrayList<Int>      = ArrayList<Int>()
        val possibleGoals:  ArrayList<Int>      = ArrayList<Int>()
        val outOfBounces:   ArrayList<Int>      = ArrayList<Int>()
        var gameNumber:     ArrayList<Int>      = ArrayList<Int>()
        var array_control:  ArrayList<String>   = ArrayList<String>()
        lateinit var arrayAdapter_control: ArrayAdapter<String>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //UI
        //listView_control--------------------------------------------------------------------------
        val listView_control: ListView = findViewById(R.id.listView_control)
        array_control.add("GOAL")
        array_control.add("POSSIBLE GOALS")
        array_control.add("BAD GOAL SHOOT")
        array_control.add("OUT OF BOUNCE")
        array_control.add("GAME: $gameNumber")
        arrayAdapter_control = ArrayAdapter(this, android.R.layout.simple_list_item_1,array_control)
        listView_control.adapter=arrayAdapter_control
        listView_control.setOnItemClickListener { parent, view, position, id ->
            clicked(this,position)
        }
        //listed values
        for (0..72) {
            goals.add(0)

        }
    }
}
private fun clicked(context: Context, index: Int) {
    when(index) {
        0 -> {goals[gameNumber]++; array_control.set(0, "Goals: $goals")}
        1 -> {goals++; array_control.set(0, "Goals: $goals")}
        2 -> {goals++; array_control.set(0, "Goals: $goals")}
        3 -> {goals++; array_control.set(0, "Goals: $goals")}
    }
    arrayAdapter_control.notifyDataSetChanged()
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
                1 -> goals+=buffer.toInt()
                2 -> possibleGoals+=buffer.toInt()
                3 -> outOfBounces+=buffer.toInt()
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