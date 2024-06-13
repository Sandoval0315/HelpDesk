package sandoval0315.helpdesk

import Conexion.Conexion
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtDescripcion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtContraseña = findViewById<EditText>(R.id.txtContraseña)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val btnIrAlLogin = findViewById<Button>(R.id.btnIrAlLogin)

        btnBack.setOnClickListener {
            val intent = Intent(this, Bievenida::class.java)
            startActivity(intent)
        }

        btnIrAlLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }



        btnIrAlLogin.visibility = View.GONE
        btnCrearCuenta.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val correo = txtCorreo.text.toString()
            val contraseña =txtContraseña.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                // mostrar error en caso de querer crear cuenta con campos vacios
                val toast = Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT)
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({ toast.cancel() }, 3000)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = Conexion().cadenaConexion()

                    // Insertar datos en la tabla
                    val addUsuario = objConexion?.prepareStatement("insert into UsuariosHD (uuid, nombre, correo, contraseña) values(?,?,?,?)")!!
                    addUsuario.setString(1, UUID.randomUUID().toString())
                    addUsuario.setString(2, nombre)
                    addUsuario.setString(3, correo)
                    addUsuario.setString(4, contraseña)
                    addUsuario.executeUpdate()

                    runOnUiThread {
                        // limpiar campos al hacer clic
                        txtNombre.setText("")
                        txtCorreo.setText("")
                        txtContraseña.setText("")

                        btnCrearCuenta.visibility = View.GONE
                        btnIrAlLogin.visibility = View.VISIBLE


                    }
                }
            }

        }

    }
}