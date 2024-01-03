package com.example.casopractico5

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.casopractico5.databinding.ActivityLoginBinding
import com.example.casopractico5.databinding.ActivityMainBinding

class Login : AppCompatActivity() {

    //Variable en la que vamos a almacenar el shared preferences
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        //cargamos este layout en el binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Manejos varios de la splash screen
        Thread.sleep(2000)
        splashScreen.setKeepOnScreenCondition{false}

        //De esta manera creamos y inicializamos el shared preferences
        prefs = getSharedPreferences("Preferences", MODE_PRIVATE)

        //Llamamos al metodo que setea el email y la pass por defecto si estan en el shared preferences
        setValues()

        //Listener al boton de Login
        binding.btnLogin.setOnClickListener {
            //Sacamos lo que haya escrito a dos variables
            val email = binding.txtName.text.toString()
            val pass = binding.txtPassword.text.toString()
            //Pasamos el metodo login, si es correcto cambiamos de activity
            if (login(email, pass)) {
                savePreferences(email, pass)
                toMainActivity()
                Toast.makeText(
                    this,
                    "Logueado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



        //-----MOTION LAYOUT-----

        //Codigo del Motion Layout para que el logo de vueltas al entrar
        //Con el binding seleccionamos el motion layout y le ponemos un transition listener
        //Esto automaticamente te obligara a implementar las funciones que hay abajo
        binding.motionLayout.setTransitionListener( object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                //El unico codigo que hemos añadido es este,el resto es automatico
               binding.motionLayout.visibility = View.GONE
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }
        })


    }


    /**
     * Metodo para la validacion del email
     */
    private fun eMailValidator(email: String): Boolean {

        //Con text utils y patters miramos que el email no este vacio y que tenga @
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    /**
     * Metodo para la validacion del password
     */
    private fun passwordValidator(pass: String): Boolean {

        //Miramos que no este vacio y que tenga mas de 5 caracteres
        return !TextUtils.isEmpty(pass) && pass.length > 5

    }

    /**
     * Metodo para guardar los datos en el shared preferences
     */
    private fun savePreferences(email: String, pass: String) {

        //Creamos en una variable una referencia al shared preferences
        val editor = prefs!!.edit()

        //Si el switch esta pulsado
        if (binding.btnSwitch.isChecked) {
            //Añadimos los datos y aplicamos
            editor.putString("email", email)
            editor.putString("pass", pass)
            //Tambien añadimos un boolean para saber que ese usuario va a ser recordado
            editor.putBoolean("recordar", true)
            editor.apply()

        } else {
            //En caso contrario lo limpiamos
            editor.clear()
            //Y especificamos en un boolean que ese user no esta recordado
            editor.putBoolean("recordar", false)
            editor.apply()

        }


    }

    /**
     * Método que abre la main activity
     */
    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)

        //Con esta linea hacemos que cuando pases de activity no puedas volver si le das al boton de atras
        //Para tener que hacer logout si o si
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    /**
     * Metodo que pone el correo y la pass por defecto si estan en shared preferences
     */
    private fun setValues() {
        val email = prefs.getString("email", "")
        val pass = prefs.getString("pass", "")
        val remember = prefs.getBoolean("recordar", false)
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            binding.txtName.text = Editable.Factory.getInstance().newEditable(email)
            binding.txtPassword.text = Editable.Factory.getInstance().newEditable(pass)
            binding.btnSwitch.isChecked = remember
        }
    }

    private fun login(email: String, pass: String): Boolean {

        //Boolean para dictaminar si el correo y la contraseña son validos
        var aviable = false

        //Si la validacion da negativa
        if (!eMailValidator(email)) {
            //Lanzamos un toast
            Toast.makeText(this, "Email no valido.Introduzca un email correcto", Toast.LENGTH_SHORT)
                .show()

            //Seguimos con la contraseña
        } else if (!passwordValidator(pass)) {
            //Lanzamos un toast
            Toast.makeText(
                this,
                "Password no valida.Debe de tener al menos 5 caracteres",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            aviable = true
        }

        return aviable
    }

}