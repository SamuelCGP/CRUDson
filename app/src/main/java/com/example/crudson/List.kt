package com.example.crudson

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf


class List : AppCompatActivity() {
    private lateinit var alert: AlertDialog

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
        textRow.setTextColor(Color.parseColor("#000000"))
        textRow.setPadding(0, 10, 0, 10)
        textRow.setBackgroundColor(Color.LTGRAY)

        return textRow
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getClients() {
        val db = Firebase.firestore
        var allClients = mutableListOf<Map<String, Any>>()
        var allClientsIds = mutableListOf<String>()

        db.collection("clients")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CLIENT_DATA", "${document.id} => ${document.data}")
                    allClients.add(document.data)
                    allClientsIds.add(document.id)
                }
                Log.d("ALLCLIENTS", allClients.toString())
                drawClients(allClients, allClientsIds)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun drawClients(clients: MutableList<Map<String, Any>>, clientsIds: MutableList<String>) {
        // GET TABLE
        val table = findViewById<TableLayout>(R.id.table)
        var rowNumber = 0

        // TEXT VIEWS FOR DATA
        for (data in clients) {
            rowNumber += 1
            val nome = data["nome"].toString()
            val endereco = data["endereco"].toString()
            val bairro = data["bairro"].toString()
            val cep = data["cep"].toString()

            val client = listOf<String>(nome, endereco, bairro, cep)

            // CREATING A ROW WITH THE INFOS
            val row = TableRow(this)
            val rowId = rowNumber.toString()
            row.id = rowId.toInt()
            row.setOnClickListener{EditOrDelete(rowId)}

            for (info in client) {
                val viewData = createRow()
                viewData.text = info
                Log.d("INFO $nome", info)
                row.addView(viewData)
            }

            table.addView(row)
        }

    }

    private fun EditOrDelete(rowId: String){
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.popup_list, null)

        build.setView(view)

        view.findViewById<Button>(R.id.btnCancel)!!.setOnClickListener { alert.dismiss() }
        view.findViewById<Button>(R.id.btnEdit)!!.setOnClickListener {
            Toast.makeText(baseContext, rowId, Toast.LENGTH_SHORT).show()
            alert.dismiss()
        }
        view.findViewById<Button>(R.id.btnDelete)!!.setOnClickListener {
            Toast.makeText(baseContext, rowId, Toast.LENGTH_SHORT).show()
            alert.dismiss()
        }

        alert = build.create()
        alert.show()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}