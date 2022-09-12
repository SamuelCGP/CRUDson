package com.example.crudson

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Create : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val inputs = getInputs()

            if(
                inputs[0].isNotBlank() &&
                inputs[1].isNotBlank() &&
                inputs[2].isNotBlank() &&
                inputs[3].isNotBlank()
            ) {
                createClient(inputs[0], inputs[1], inputs[2], inputs[3])
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        val btnToList = findViewById<Button>(R.id.btnToList)

        btnToList.setOnClickListener {
            val intent = Intent(this, List::class.java)
            startActivity(intent)
        }
    }

    private fun getInputs(): Array<String> {
        val edtNome = findViewById<EditText>(R.id.edtNome).text.toString()
        val edtEndereco = findViewById<EditText>(R.id.edtEndereco).text.toString()
        val edtBairro = findViewById<EditText>(R.id.edtBairro).text.toString()
        val edtCep = findViewById<EditText>(R.id.edtCep).text.toString()

        return arrayOf(edtNome, edtEndereco, edtBairro, edtCep)
    }

    private fun createClient(nome: String, endereco: String, bairro: String, cep: String) {
        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "nome" to nome,
            "endereco" to endereco,
            "bairro" to bairro,
            "cep" to cep
        )

        // Add a new document with a generated ID
        db.collection("clients")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(this, "Error adding document: $e", Toast.LENGTH_SHORT).show()
            }
    }
}