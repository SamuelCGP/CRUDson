package com.example.crudson

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.List

class Edit : AppCompatActivity() {
    lateinit var clientId: String
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val clientIdFromIntent = intent.getStringExtra("clientId")
        clientId = clientIdFromIntent.toString()

        fillInputs()

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val inputs = getInputs()

            if(
                inputs[0].isNotBlank() &&
                inputs[1].isNotBlank() &&
                inputs[2].isNotBlank() &&
                inputs[3].isNotBlank()
            ) {
                updateClient(inputs[0], inputs[1], inputs[2], inputs[3])
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        val btnToList = findViewById<Button>(R.id.btnBack)

        btnToList.setOnClickListener {
            val intent = Intent(this, ListTable::class.java)
            startActivity(intent)
        }
    }

    private fun fillInputs() {
        db.collection("clients").document(clientId)
            .get()
            .addOnSuccessListener { document ->
                val data = document.data

                Log.d("CLI-DATA", data.toString())

                findViewById<EditText>(R.id.edtNome).setText(document.get("nome").toString())
                findViewById<EditText>(R.id.edtEndereco).setText(document.get("endereco").toString())
                findViewById<EditText>(R.id.edtBairro).setText(document.get("bairro").toString())
                findViewById<EditText>(R.id.edtCep).setText(document.get("cep").toString())
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error updating document: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getInputs(): Array<String> {
        val edtNome = findViewById<EditText>(R.id.edtNome).text.toString()
        val edtEndereco = findViewById<EditText>(R.id.edtEndereco).text.toString()
        val edtBairro = findViewById<EditText>(R.id.edtBairro).text.toString()
        val edtCep = findViewById<EditText>(R.id.edtCep).text.toString()

        return arrayOf(edtNome, edtEndereco, edtBairro, edtCep)
    }

    private fun updateClient(nome: String, endereco: String, bairro: String, cep: String) {
        val client = db.collection("clients").document(clientId)
        client
            .update(mapOf(
                "bairro" to bairro,
                "cep" to cep,
                "endereco" to endereco,
                "nome" to nome
            ))
            .addOnSuccessListener { result ->
                Toast.makeText(this, "Success updating document", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ListTable::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error updating document: $exception", Toast.LENGTH_SHORT).show()
            }
    }

}
