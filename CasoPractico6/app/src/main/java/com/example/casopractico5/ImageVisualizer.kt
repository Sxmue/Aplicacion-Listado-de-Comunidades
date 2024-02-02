package com.example.casopractico5

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.example.casopractico5.databinding.ActivityImageVisualizerBinding
import com.example.casopractico5.entities.ComunityDAO

class ImageVisualizer : AppCompatActivity() {

    //Inicializacion del dao que usaremos para interactuar con la base de datos
    lateinit var miDAO: ComunityDAO

    //Binding para la vista
    lateinit var binding: ActivityImageVisualizerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Seteamos el binding
        binding = ActivityImageVisualizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion del dao que usaremos para interactuar con la base de datos
        miDAO=ComunityDAO()

        //Sacamos el nombre de la comunidad para el DAO
        val name = intent.extras!!.getString("nombre")

        //Nos traemos la comunidad que tiene su iri dentro con nuestro DAO
        val c = name?.let { miDAO.getComunityByName(this, it) }

        //Y si la uri no es nula la metemos en la imagens
        if (c!!.uri!!.isNotEmpty()){
            val uri = Uri.parse(c.uri)
            binding.imagenCompleta.load(uri)
        }else{

            binding.imagenCompleta.setImageResource(R.drawable.nophoto)
            //Si es nula ponemos un toast
            Toast.makeText(this,"La comunidad ${c.name} no tiene una imagen guardada aún",Toast.LENGTH_SHORT).show()
        }

    }
}