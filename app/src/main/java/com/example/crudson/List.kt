package com.example.crudson

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf


class List : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // GETTING AND DRAWING DATA
        getClients()
    }

    private fun createRow(): TextView {
        val textRow = TextView(this)
        textRow.gravity = Gravity.CENTER_HORIZONTAL
        textRow.setTextSize(TypedValue.COMPLEX_UNIT_SP,15F)
        textRow.setTextColor(Color.parseColor("#000000"));

        return textRow
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getClients() {
        val db = Firebase.firestore
        var allClients = mutableListOf<Map<String, Any>>()

        db.collection("clients")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CLIENT_DATA", "${document.id} => ${document.data}")
                    allClients.add(document.data)
                }
                Log.d("ALLCLIENTS", allClients.toString())
                drawClients(allClients)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun drawClients(clients: MutableList<Map<String, Any>>) {
        // GET TABLE
        val table = findViewById<TableLayout>(R.id.table)

        // TEXT VIEWS FOR DATA
        for (data in clients) {
            val nome = data["nome"].toString()
            val endereco = data["endereco"].toString()
            val bairro = data["bairro"].toString()
            val cep = data["cep"].toString()

            val client = listOf<String>(nome, endereco, bairro, cep)

            // CREATING A ROW WITH THE INFOS
            val row = TableRow(this)

            for (info in client) {
                val viewData = createRow()
                viewData.text = info
                Log.d("INFO $nome", info)
                row.addView(viewData)
            }

            table.addView(row)
        }

    }

}