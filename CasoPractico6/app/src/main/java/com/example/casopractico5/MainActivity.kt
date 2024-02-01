package com.example.casopractico5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casopractico5.entities.Comunity
import com.example.casopractico5.databinding.ActivityMainBinding
import com.example.casopractico5.entities.ComunityDAO
import com.example.casopractico5.maps.MapsActivity
import com.example.casopractico5.maps.OpenSteetMapActivity
import com.example.casopractico5.reciclerview.ComunityAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var miDAO:ComunityDAO


    /*El binding para hacer referencia a vistas de la main activiti*/
    private lateinit var binding: ActivityMainBinding

    /*Mutable list vacia que nos declaramos para trabajar con ella aqui*/
    private lateinit var emptyList: List<Comunity>

    /*Mutable list en la que vamos a guardar los cambios de uso en el momento*/
    private lateinit var comunityList:List<Comunity>

    //Inicializamos el inicio de nuestro intent
    private lateinit var intentLaunch: ActivityResultLauncher<Intent>

    //Nuevo nombre de la comunidad
    private var newname = "Sin Nombre"

    //Variable en la que nos traemos la comunidad afectada por la pulsacion contextual
    lateinit var com: Comunity


    /**
     * OnCreate de la actividad principal, aqui va el codigo relacionado con la main activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //----------EN ESTA VERSION USAREMOS BASES DE DATOS PARA EL USO DE LA APLICACION----------

        //Inicializacion del dao que usaremos para interactuar con la base de datos
        miDAO=ComunityDAO()

        //Pone el deDentro a false por si cerramos la aplicacion de golpe
        var prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putBoolean("deDentro",false)
        editor.apply()

        //Inicialización de la lista principal
        comunityList = miDAO.cargarLista(this)

        //Este trozo es solamente para que cuando pulses para atras no vayas al login, sino que tengas que hacer logout
        this.onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }

        })

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.RVComunities.layoutManager = LinearLayoutManager(this)
        binding.RVComunities.adapter = ComunityAdapter(comunityList) { comunity ->
            onItemSelected(comunity)
        }

        //Realizacion del IntentLaunch
        intentLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            //Segun el resultado, si esta OK
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {

                //Nos traemos la devolucion de los datos a una variable
                newname = result.data?.extras?.getString("nombre").toString()

                //Metemos en la variable auxiliar com, el nombre nuevo de la comunidad que queremos ponerle
                com.name=newname

                //Tambien podrias haber enviado el indice de la comunidad afectada, asi podrias haberlo
                //Recibido aqui saltandote este paso
                //Sacamos  en que indice esta la comunidad afectada gracias a la variable com
                val index = comunityList.indexOf(com)

                //Y a esa comunidad le cambiamos el nombre en la base de DATOS
                miDAO.actualizarComunidad(this,com,index+1)

                //Recargamos la lista de la base de datos
                comunityList = miDAO.cargarLista(this)

                //Desde que hemos añadido DiffUtils, esto ya no es necesario
               //--------- PREGUNTAR AL PROFE ---------
                //indicamos al adaptador que se ha modificado el objeto para que muestre el cambio
                binding.RVComunities.adapter!!.notifyItemChanged(index)

            }
        }


    }


    /**
     * Funcion Para realizar dentro de la lambda
     */
    fun onItemSelected(comunity: Comunity) {
        //Declaramos el toast
        Toast.makeText(
            this,
            "Soy de  ${comunity.name}",
            Toast.LENGTH_SHORT
        ).show()
    }


    /**
     * Metodo OnCreate relacionado con el menu de opciones desplegable
     * solo lo inflamos y le ponemos el titulo
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        setTitle("Comunidades autónomas")
        return true
    }

    /**
     * En este OnCreate se decide que hacer en cada opcion del menu de opciones (el de arriba)
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Limpiar -> {
                cleanList()
                true
            }

            R.id.Recargar -> {
                reload()
                true
            }
            R.id.RecargarBackup -> {

               var alert = AlertDialog.Builder(this).setTitle("Reseteo Backup")
                    .setMessage("Esta operacion eliminara los cambios y reseteara la lista por defecto,¿Estas Seguro?")
                    .setNeutralButton("Cerrar", null).setPositiveButton("Aceptar") { _, _ ->
                reloadBackup()
                    }.create()
                //Enseñamos la alerta por pantalla con este metodo
                alert.show()

                true
            }
            R.id.Logout -> {
                var prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
                val editor = prefs!!.edit()
                editor.putBoolean("deDentro",true)
                editor.apply()
                val intent = Intent(this,Login::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    /**
     * Metodo para limpiar la lista, basicamente devuelve una vacia en otra instancia de adapter
     */
    private fun cleanList() {
        emptyList = miDAO.loadEmpty(this)
        binding.RVComunities.adapter = ComunityAdapter(emptyList) { comunity ->
            onItemSelected(comunity)
        }

    }

    /**
     * Vuelve a recargar la lista
     */
    private fun reload() {
        //Recargamos la lista de la base de datos
        comunityList = miDAO.cargarLista(this)

        //Se la asignamos actualizada al adaptador
        binding.RVComunities.adapter = ComunityAdapter(comunityList) { comunity ->
            onItemSelected(comunity)
        }


    }

    /**
     * Vuelve a recargar la lista original
     */
    private fun reloadBackup(){

        miDAO.matchTables(this)

        //Recargamos la lista de la base de datos
        comunityList = miDAO.cargarLista(this)

        //Se la asignamos actualizada al adaptador
        binding.RVComunities.adapter = ComunityAdapter(comunityList) { comunity ->
            onItemSelected(comunity)
        }

        //Esto era necesario antes, pero al haber añadido DiffUtil ya no hace falta

        /*Indicamos al adaptador que la lista ha cambiado
        binding.RVComunities.adapter!!.notifyItemRangeChanged(
            0,
            comunityList.size
        )*/

    }


    /**
     * Metodo en el que implementamos la logica al menu contextual
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {

        //Creamos una variable para traer la comunidad que tenemos afectada
        lateinit var comunityAfected: Comunity

        //Una variable para la vista que vamos a lanzar
        lateinit var miIntent: Intent

        //Nos traemos la lista de comunidades agrupadas por el ID
        comunityAfected = comunityList[item.groupId]

        //Switch para distinguir la opcion del menu contextual pulsada
        when (item.itemId) {

            //Si el boton que pulsamos es el de Id 0
            0 -> {

                //Creamos un dialogo de alerta, con un titulo,un mensaje y dos botones Cerrar y Aceptar
                val alert =
                    AlertDialog.Builder(this).setTitle("Eliminar ${comunityAfected.name}")
                        .setMessage("¿Estas seguro de que quieres eliminar ${comunityAfected.name}")
                        .setNeutralButton("Cerrar", null).setPositiveButton("Aceptar") { _, _ ->

                            //Llamamos al metodo del snackbar para que muestre el mensajito abajo si pulsamos aceptar
                            sneacker("Se ha eliminado ${comunityAfected.name}")

                            //-------REALIZACION DE ELIMINAR CON BASE DE DATOS--------

                            //MUY IMPORTANTE, a la hora de eliminar, trata de hacerlo con un campo que tenga el objeto,
                            //Si el objeto no tiene un id NO intentes hacerlo con el itemId porque da error
                            //Eliminamos la comunidad pulsada a traves del nombre del item
                            miDAO.deleteComunidad(this,comunityAfected.name)

                            //Recargamos la lista de la base de datos
                            comunityList = miDAO.cargarLista(this)


                            //Se la asignamos actualizada al adaptador
                            binding.RVComunities.adapter = ComunityAdapter(comunityList) { comunity ->
                                onItemSelected(comunity)
                            }

                            //Esto era antes de añadir la libreria DiffUtil, ya no hace falta notificar al adapter

                            /*
                            //indicamos al adaptador que se ha eliminado el objeto, para que no lo muestre
                            binding.RVComunities.adapter!!.notifyItemRemoved(item.groupId)
                            //Indicamos al adaptador que la lista tiene un objeto menos
                            binding.RVComunities.adapter!!.notifyItemRangeChanged(
                                item.groupId,
                                comunityList.size
                            )
                            */


                            //Despues de la llave llamamos al metodo create para que se cree la alerta
                        }.create()

                //Enseñamos la alerta por pantalla con este metodo
                alert.show()
            }

            1 -> {

                //Nos llevamos la comunidad afectada a una variable externa
                com = comunityAfected

                //Le pasamos la lista en la que vamos a trabajar ya que anteriormente tiene otra asignada
                binding.RVComunities.adapter = ComunityAdapter(comunityList) { comunity ->
                    onItemSelected(comunity)
                }

                //Creamos el intent, con los dos valores que lanzamos a la otra aplicacion
                val intent = Intent(this, EditComunity::class.java)
                //Si llegamos a enviar la posicion de la comunidad afectada nos habria sido util en la otra parte
                intent.putExtra("nombre", newname)
                intent.putExtra("img", comunityAfected.flag)
                intent.putExtra("comunityname",comunityAfected.name)

                //Lanzamos el intent para crear una nueva activity
                intentLaunch.launch(intent)

            }
            2 -> {
                //En este trozo manejaremos la interaccion del menu contextual con google maps
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("nombre", comunityAfected.name)
                startActivity(intent)

            }
            3 -> {
                //En este trozo manejaremos la interaccion del menu contextual con Open Street Maps
                val intent = Intent(this, OpenSteetMapActivity::class.java)
                intent.putExtra("nombre", comunityAfected.name)
                startActivity(intent)

            }
            //Aqui realizamos las acciones relacionadas con la camara
            4 ->{
                //Iniciamos la actividad de la camara
                val intent = Intent(this, FotoActivity::class.java)
                intent.putExtra("nombre", comunityAfected.name)
                startActivity(intent)

            }

        }

        return true
    }

    private fun sneacker(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


}