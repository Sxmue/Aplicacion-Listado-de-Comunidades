package com.example.casopractico5

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.casopractico5.databinding.ActivityFotoBinding
import com.example.casopractico5.entities.ComunityDAO
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FotoActivity : AppCompatActivity() {

    //Sacamos el binding
    private lateinit var binding:ActivityFotoBinding;

    //Sacamos el executor service de la camara para usarla
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var imageCapture: ImageCapture

    //Añadimos las variables para almacenar el nombre de la comunidad y el id
    private lateinit var nombreComunidad:String
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Ponemos el binding
        binding = ActivityFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nombreComunidad = intent.extras!!.getString("nombre").toString()
        id = intent.extras!!.getInt("id")

        //Vamos a pedirle los permisos al ususario para poder acceder a la camara
        // Solicitud de los permisos de la cámara
        if (allPermisions()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Se establece el listener para la captura de foto
        binding.btnFoto.setOnClickListener {
            //llamamos al metodo de capturar una foto
            takePhoto()
        }
        cameraExecutor = Executors.newSingleThreadExecutor()


    }


    /**
     * Metodo que se encarga de realizar una foto
     */
    private fun takePhoto() {
        // Obtenemos una referencia estable para el caso de uso de captura de imágenes
        val imageCapture = imageCapture ?: return




        // Creamos un nombre para el archivo con la hora y dónde se va a almacenar
        //Aqui usamos la constante de companion Object
        val name = "${nombreComunidad}_" + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())

        //Esto es un set con configuraciones, le pasas el nombre con el que se guardan las fotos
        //El tipo y el path donde se guardan
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }


        //Y ese set con las configuraciones de antes lo necesitas aqui, lo demas no se que hace
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()



        // Se establece el listener para la captura de imagen que se lanzará cuando se ha hecho una foto
        imageCapture.takePicture(
            //Le pasas el output de arriba, que tiene las configuraciones de salida
            outputOptions,
            //El contexto
            ContextCompat.getMainExecutor(this),

            //Y el este closure como ultimo parametro
            object : ImageCapture.OnImageSavedCallback {
                //Sobreescribimos esta funcion,que se da en caso de error
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                //Sobreescribimos esta funcion que se da con la captura correcta,recibe el output que le das
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Captura de foto correcta: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    //HASTA AQUI SERIA SOLO HACER LA FOTO


                    //AHORA VAMOS A INSERTAR ESA FOTO EN LA BBDD

                    //INSERTAR EN LA BASE DE DATOS LA URI  output.savedUri
                    //Te guardas la uri a una variable, que es la url completa con la ruta de la foto en el movil
                    val photoUri = output.savedUri

                    //Nos instanciamos el DAO
                    val miDAO = ComunityDAO()
                    val comunidadElegida = miDAO.getComunityByName(applicationContext,nombreComunidad)
                    comunidadElegida.uri = output.savedUri.toString()

                    miDAO.actualizarComunidad(applicationContext, comunidadElegida,id)

                }
            }
        )
    }





















    //------------- METODOS PARA CHEKEAR LOS PERMISOS -----------------
    /**
     * Con esta funcion comprobamos que todos los permisos esten dados
     */
    private fun allPermisions() = Companion.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Este metodo es una fumada, copialo tal cual
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermisions()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permisos no concedidos por el usuario.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    /**
     * Funcion que se encarga de iniciar la camara
     */
    private fun startCamera() {
        //Cogemos una instancia del camara provider
        val cameraProviderInstance = ProcessCameraProvider.getInstance(this)

        //Y le añadimos un Listener con una serie de configuraciones para usarlo
        cameraProviderInstance.addListener({

            // Vinculamos para vincular el ciclo de vida de la cámara al ciclo de vida de la app
            //No entiendo esto bien pero hay que acerlo
            val cameraProvider: ProcessCameraProvider = cameraProviderInstance.get()


            // Creamos nuestro preview de esta manera
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.preview.surfaceProvider)
                }



            //Inicializamos el image capture creado arriba
            imageCapture = ImageCapture.Builder().build()


            // Seleccionamos la cámara trasera por defecto a una variable
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA



            try {

                // Desvincula antes de volver a vincular
                cameraProvider.unbindAll()

                // Vinculamos los casos de uso a la cámara, las 3 variables que hemos creado
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Vinculación errónea", exc)
            }

            //Aqui acaba el listener y ya esta linea ni puñetera idea de que hace
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Companion object con algunas constantes que necesitaremos, como el codigo de los permisos y la mutable list con los permisos
     */
    companion object {
        private const val TAG = "com.example.casopractico5"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }





}

