package com.example.matchcount

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
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
import com.example.matchcount.MainActivity.Companion.outOfBounds
import com.example.matchcount.MainActivity.Companion.possibleGoals
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        val context = this
        var goals:          ArrayList<Int>      = ArrayList<Int>()
        val possibleGoals:  ArrayList<Int>      = ArrayList<Int>()
        val outOfBounds:    ArrayList<Int>      = ArrayList<Int>()
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
            outOfBounds.add(0)
            enemyGoals.add(0)
        }
        //UI
        val textView_test: TextView = findViewById(R.id.textView_test)
        //listView_control--------------------------------------------------------------------------
        val listView_control: ListView = findViewById(R.id.listView_control)
        //read storage
        val data_goals=readStorage(this,"matchCount.txt")
        val data_possibleGoals=readStorage(this,"matchCount_goals.txt")
        val data_outOfBounds=readStorage(this, "matchCount_outOfBounds.txt")
        val data_enemyGoals= readStorage(this, "matchCount_enemyGoals.txt")
        extractData(data_goals, data_possibleGoals, data_outOfBounds, data_enemyGoals, textView_test, this)

        buffer=goals[gameNumber].toString();array_control.add("GOALS: $buffer")
        buffer= possibleGoals[gameNumber].toString();array_control.add("POSSIBLE GOALS: $buffer")
        buffer= outOfBounds[gameNumber].toString();array_control.add("OUT OF BOUNDS: $buffer")
        buffer= enemyGoals[gameNumber].toString();array_control.add("ENEMY GOALS: $buffer")
        buffer= gameNumber.toString();array_control.add("GAME: $gameNumber")
        array_control.add("Settings")
        arrayAdapter_control = ArrayAdapter(this, android.R.layout.simple_list_item_1,array_control)
        listView_control.adapter=arrayAdapter_control
        listView_control.setOnItemClickListener { _, _, position, _ ->
            clicked(this,position,textView_test)
            //textView_test.text=position.toString()
        }
        listView_control.setOnItemLongClickListener { _, _, position, _ ->
            longKClicked(this, position, textView_test)
            true//required.don't know why
        }
    }
}
private fun clicked(context: Context, index: Int, textView_test: TextView) { //index in listView
    when(index) {   //operate the proper button
        0 -> {goals[gameNumber]++}
        1 -> {possibleGoals[gameNumber]++}
        2 -> {outOfBounds[gameNumber]++}
        3 -> {enemyGoals[gameNumber]++}
        4 -> {gameNumber++;gameNumber=if(gameNumber==73)0 else gameNumber}
        5 -> { Toast.makeText(context,"settings not implemented yet",Toast.LENGTH_SHORT).show()}
    }//refresh whole listView
    buffer= goals[gameNumber].toString(); array_control[0] = "Goals: $buffer"
    buffer= possibleGoals[gameNumber].toString(); array_control[1] = "Possible Goals: $buffer"
    buffer= outOfBounds[gameNumber].toString(); array_control[2] = "Out Of Bounds: $buffer"
    buffer= enemyGoals[gameNumber].toString(); array_control[3] = "Enemy Goals: $buffer"
    buffer= gameNumber.toString(); array_control[4]="Game: $buffer"
    arrayAdapter_control.notifyDataSetChanged()
    overrideStorage(context, textView_test)
    //textView_test
}
private fun longKClicked(context: Context, index: Int, textView_test:TextView) { //index in listView
    when(index) {   //operate the proper button
        0 -> {goals[gameNumber]=0}
        1 -> {possibleGoals[gameNumber]=0}
        2 -> {outOfBounds[gameNumber]=0}
        3 -> {enemyGoals[gameNumber]=0}
        4 -> {gameNumber=0}
        5 -> { Toast.makeText(context,"settings not implemented yet",Toast.LENGTH_SHORT).show()}
    }//refresh whole listView
    buffer= goals[gameNumber].toString(); array_control[0] = "Goals: $buffer"
    buffer= possibleGoals[gameNumber].toString(); array_control[1] = "Possible Goals: $buffer"
    buffer= outOfBounds[gameNumber].toString(); array_control[2] = "Out Of Bounds: $buffer"
    buffer= enemyGoals[gameNumber].toString(); array_control[3]="Enemy Goals: $buffer"
    buffer= gameNumber.toString(); array_control[4]="Game: $buffer"
    arrayAdapter_control.notifyDataSetChanged()
    overrideStorage(context, textView_test)
}
fun overrideStorage(context: Context, textView_test: TextView){
    // Schreiben in eine Textdatei
    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    var file = File(/*context.filesDir*/downloadFolder, "matchCount.txt")    //TODO:implement settings with inputTextLayer for naming the file
    var data: String= "Goals: $goals Possible Goals: $possibleGoals\nOut Of Bounds: $outOfBounds\nEnemy Goals: $enemyGoals"
    file.writeText(data.toString())

    file =  File(/*context.filesDir*/downloadFolder, "matchCount_goals.txt")//store goal data
    data=goals.toString()
    file.writeText(data)

    file =  File(/*context.filesDir*/downloadFolder, "matchCount_possibleGoals.txt")//store possibleGoal data
    data= possibleGoals.toString()
    file.writeText(data)

    file =  File(/*context.filesDir*/downloadFolder, "matchCount_outOfBounds.txt")
    data=outOfBounds.toString()
    file.writeText(data)

    file =  File(/*context.filesDir*/downloadFolder, "matchCount_enemyGoals.txt")
    data= enemyGoals.toString()
    file.writeText(data)
    //textView_test.text=file.canWrite().toString()
    textView_test.text=data.toString()
}
fun readStorage(context: Context, fileName:String): String {
    val downloadFolder =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(/*context.filesDir*/downloadFolder, fileName)
    return file.readText()
}
fun extractData(goals: String, possibleGoals: String, outOfBounds:String, enemyGoals: String, textView_test: TextView, context: Context): Unit{                                                            //make sure, that the lists are int only
    textView_test.text = ""
    var arraysIndex: Int = 0//which array is the current (0:goals,...)
    var arrayIndex: Int = 0//which is current position in array, like gameNumber
    var buffer: String = ""
    val bufferArray: ArrayList<String> = ArrayList()

    buffer=goals.substring(1, goals.length-1)           //goals
    bufferArray.addAll(buffer.split(","))
    for(i in 0 until bufferArray.size) {
        MainActivity.goals[i]=bufferArray[i].toInt()
    }
    bufferArray.clear()

    buffer=possibleGoals.substring(1,goals.length-1)
    bufferArray.addAll(buffer.split(","))
    for(i in 0 until bufferArray.size) {
        MainActivity.possibleGoals[i]=bufferArray[i].toInt()
    }
    bufferArray.clear()

    buffer=outOfBounds.substring(1, outOfBounds.length-1)
    bufferArray.addAll(buffer.split(","))
    for (i in 0 until bufferArray.size) {
        MainActivity.outOfBounds[i]=bufferArray[i].toInt()
    }
    bufferArray.clear()

    buffer=enemyGoals.substring(1, enemyGoals.length-1)
    bufferArray.addAll(buffer.split(","))
    for(i in 0 until bufferArray.size-1) {
        MainActivity.enemyGoals[i]=bufferArray[i].toInt()
    }
    bufferArray.clear()
}