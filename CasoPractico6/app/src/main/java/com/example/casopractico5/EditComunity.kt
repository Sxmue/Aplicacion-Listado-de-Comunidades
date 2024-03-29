package com.example.casopractico5

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout


class EditComunity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_comunity)

        //Sacamos el cajetin para introducir el nuevo texto
        val editname = findViewById<EditText>(R.id.editTextBanderaNuevo)

        //Sacamos la img para trabajar con ella
        val imgedit = findViewById<ImageView>(R.id.imgedit)

        //Sacamos el recuadro
        val edit =  findViewById<TextInputLayout>(R.id.txtbanderanuevo)

        //Del intent recibimos la imagen
        val img = intent.getIntExtra("img", 0)

        //Del intent recibimos el nombre de la comunidad seleccionada
        val comunityname = intent.getStringExtra("comunityname")
        //Lo usamos en editText como hint
        editname.hint = comunityname

        //Vamos a ponerle un helper text al recuadro
        edit.helperText = "C.Autónoma"

        //De esta manera sacamos el color de drawable
        val colorAzul = ContextCompat.getColor(this, R.color.azul)

        //Le cambiamos el color
        edit.setHelperTextColor(ColorStateList.valueOf(colorAzul))

        //Se la ponemos a la imagen de la vista actual
        imgedit.setImageResource(img)

        val btncancel = findViewById<Button>(R.id.btncancelar)

        //Traemos el boton cambiar
        val btnchange = findViewById<Button>(R.id.btncambiar)

        //Listener para el boton que devuelve el nombre que hemos escrito de vuelta a la otra activity
        btnchange.setOnClickListener {
            val intent = Intent()

            //Pasamos a texto el cajetin y lo metemos a una variable
            val nombre = editname.text.toString()

            //La devolvemos de este modo
            intent.putExtra("nombre", nombre)

            //Devolvemos el resultado y cerramos la aplicacion
            setResult(RESULT_OK, intent)
            finish()
        }

        //Listener para el boton cancelar vuelva a la activity anterior
        btncancel.setOnClickListener {
            finish()
        }


    }
}