package com.example.matchcount

import android.content.Context
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.matchcount.MainActivity.Companion.arrayAdapter_control
import com.example.matchcount.MainActivity.Companion.array_control
import com.example.matchcount.MainActivity.Companion.buffer
import com.example.matchcount.MainActivity.Companion.enemyGoals
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
        val enemyGoals:     ArrayList<Int>      = ArrayList<Int>()
        var gameNumber:     Int                 = 0
        var array_control:  ArrayList<String>   = ArrayList<String>()
        lateinit var arrayAdapter_control: ArrayAdapter<String>

        var buffer:String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //listed values
        for (i in 0..72) {
            goals.add(0)
            possibleGoals.add(0)
            outOfBounces.add(0)
            enemyGoals.add(0)
        }
        //UI
        var textView_test: TextView = findViewById(R.id.textView_test)
        //listView_control--------------------------------------------------------------------------
        val listView_control: ListView = findViewById(R.id.listView_control)
        array_control.add("GOALS: $buffer")
        array_control.add("POSSIBLE GOALS: $buffer")
        array_control.add("OUT OF BOUNCE: $buffer")
        array_control.add("ENEMY GOALS: $buffer")
        array_control.add("GAME: $gameNumber")
        array_control.add("Settings")
        arrayAdapter_control = ArrayAdapter(this, android.R.layout.simple_list_item_1,array_control)
        listView_control.adapter=arrayAdapter_control
        listView_control.setOnItemClickListener { parent, view, position, id ->
            clicked(this,position,textView_test)
            //textView_test.text=position.toString()
        }
        listView_control.setOnItemLongClickListener { parent, view, position, id ->
            longKClicked(this, position, textView_test)
            true//required.don't know why
        }
    }
}
private fun clicked(context: Context, index: Int, textView_test: TextView) { //index in listView
    when(index) {   //operate the proper button
        0 -> {goals[gameNumber]++}
        1 -> {possibleGoals[gameNumber]++}
        2 -> {outOfBounces[gameNumber]++}
        3 -> {enemyGoals[gameNumber]++}
        4 -> {gameNumber++;gameNumber=if(gameNumber==73)0 else gameNumber}
        5 -> { Toast.makeText(context,"settings not implemented yet",Toast.LENGTH_SHORT).show()}
    }//refresh whole listView
    buffer= goals[gameNumber].toString(); array_control[0] = "Goals: $buffer"
    buffer= possibleGoals[gameNumber].toString(); array_control[1] = "Possible Goals: $buffer"
    buffer= outOfBounces[gameNumber].toString(); array_control[2] = "Out Of Bounces: $buffer"
    buffer= enemyGoals[gameNumber].toString(); array_control[3] = "Enemy Goals: $buffer"
    buffer= gameNumber.toString(); array_control[4]="Game: $buffer"
    arrayAdapter_control.notifyDataSetChanged()
    overrideStorage(context, textView_test)
    textView_test
}
private fun longKClicked(context: Context, index: Int, textView_test:TextView) { //index in listView
    when(index) {   //operate the proper button
        0 -> {goals[gameNumber]=0}
        1 -> {possibleGoals[gameNumber]=0}
        2 -> {outOfBounces[gameNumber]=0}
        3 -> {enemyGoals[gameNumber] =0}
        4 -> {gameNumber=0}
        5 -> { Toast.makeText(context,"settings not implemented yet",Toast.LENGTH_SHORT).show()}
    }//refresh whole listView
    buffer= goals[gameNumber].toString(); array_control[0] = "Goals: $buffer"
    buffer= possibleGoals[gameNumber].toString(); array_control[1] = "Possible Goals: $buffer"
    buffer= outOfBounces[gameNumber].toString(); array_control[2] = "Out Of Bounces: $buffer"
    buffer= enemyGoals[gameNumber].toString(); array_control[3]="Enemy Goals: $buffer"
    buffer= gameNumber.toString(); array_control[4]="Game: $buffer"
    arrayAdapter_control.notifyDataSetChanged()
    overrideStorage(context, textView_test)
}
fun overrideStorage(context: Context, textView_test: TextView){    //TODO: change to store all data
    // Schreiben in eine Textdatei
    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(/*context.filesDir*/downloadFolder, "matchCount.txt")    //TODO:implement settings with inputTextLayer for naming the file
    val data: String= "Goals: $goals\nPossible Goals: $possibleGoals\nOut Of Bounces: $outOfBounces\nEnemy Goals: $enemyGoals"
    file.writeText(data.toString())
    //textView_test.text=file.canWrite().toString()
    textView_test.text=data.toString()
}
fun readStorage(context: Context) {
    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(/*context.filesDir*/downloadFolder, "matchCount.txt")
    var data = file.readText()
}
fun extractData(input: String) {                                                            //make sure, that the lists are int only
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