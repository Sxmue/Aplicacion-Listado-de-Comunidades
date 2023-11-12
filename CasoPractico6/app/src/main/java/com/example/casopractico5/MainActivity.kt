package com.example.casopractico5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casopractico5.entities.Comunity
import com.example.casopractico5.databinding.ActivityMainBinding
import com.example.casopractico5.entities.ComunityDAO
import com.example.casopractico5.reciclerview.ComunityAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var miDAO:ComunityDAO

   lateinit var listaDB:MutableList<Comunity>


    /*El binding para hacer referencia a vistas de la main activiti*/
    private lateinit var binding: ActivityMainBinding

    /*Mutable list vacia que nos declaramos para trabajar con ella aqui*/
    private lateinit var emptyList: MutableList<Comunity>

    /*Mutable list en la que vamos a guardar los cambios de uso en el momento*/
    private lateinit var comunityList:MutableList<Comunity>

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

        //Inicialización de la lista principal
        comunityList = miDAO.cargarLista(this)



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

        //Indicamos al adaptador que la lista ha cambiado
        binding.RVComunities.adapter!!.notifyItemRangeChanged(
            0,
            comunityList.size
        )

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
                            //Eliminamos la comunidad pulsada a traves del id del item
                            miDAO.deleteComunidad(this,comunityAfected.name)

                            //Recargamos la lista de la base de datos
                            comunityList = miDAO.cargarLista(this)


                            //Se la asignamos actualizada al adaptador
                            binding.RVComunities.adapter = ComunityAdapter(comunityList) { comunity ->
                                onItemSelected(comunity)
                            }

                            //indicamos al adaptador que se ha eliminado el objeto, para que no lo muestre
                            binding.RVComunities.adapter!!.notifyItemRemoved(item.groupId)
                            //Indicamos al adaptador que la lista tiene un objeto menos
                            binding.RVComunities.adapter!!.notifyItemRangeChanged(
                                item.groupId,
                                comunityList.size
                            )

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

        }

        return true
    }

    private fun sneacker(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


}