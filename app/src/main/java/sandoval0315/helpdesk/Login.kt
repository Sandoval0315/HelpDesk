package sandoval0315.helpdesk

import Conexion.Conexion
import android.content.Intent
import android.os.Bundle
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
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtDescripcion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnAcceder = findViewById<Button>(R.id.btnAcceder)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoL)
        val txtContraseña = findViewById<EditText>(R.id.txtContraseñaL)

        btnBack.setOnClickListener {
            val intent = Intent(this, Bievenida::class.java)
            startActivity(intent)
        }

        btnAcceder.setOnClickListener {
            val txtCorreo = txtCorreo.text.toString()
            val txtContraseña = txtContraseña.text.toString()

            if (txtCorreo.isEmpty() || txtContraseña.isEmpty()) {
                Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = Conexion().cadenaConexion()

                    val addAcceder = "select * from UsuariosHD where correo = ? AND contraseña = ?"
                    val objAcceder = objConexion?.prepareStatement(addAcceder)
                    objAcceder?.setString(1, txtCorreo)
                    objAcceder?.setString(2, txtContraseña)

                    val resultado = objAcceder?.executeQuery()

                    if (resultado?.next() == true) {
                        withContext(Dispatchers.IO) {
                            // si esta correctas las credenciales pasa al main activity(inicio)
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            // mensaje de error en credenciales incorrectas
                            Toast.makeText(this@Login, "La contraseña o el correo son incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}