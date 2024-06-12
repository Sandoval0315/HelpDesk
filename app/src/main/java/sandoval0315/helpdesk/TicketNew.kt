package sandoval0315.helpdesk

import Conexion.Conexion
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
import java.util.UUID

class TicketNew : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket_new)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtDescripcion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDesc)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoT)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val txtFecha = findViewById<EditText>(R.id.txtFecha)
        val cbActivo = findViewById<CheckBox>(R.id.cbActivo)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarT)
        val btnBack = findViewById<ImageView>(R.id.btnBack2)

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnAgregar.setOnClickListener {
            val titulo = txtTitulo.text.toString()
            val descripcion = txtDescripcion.text.toString()
            val correo = txtCorreo.text.toString()
            val autor = txtAutor.text.toString()
            val fecha = txtFecha.text.toString()
            val activo = cbActivo.text.toString()


            if (titulo.isEmpty() || descripcion.isEmpty()||autor.isEmpty()||fecha.isEmpty()|| correo.isEmpty() || activo.isEmpty()) {
                // mostrar error en caso de querer crear cuenta con campos vacios
                val toast = Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT)
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({ toast.cancel() }, 3000)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = Conexion().cadenaConexion()

                    // Insertar datos en la tabla
                    val addTicket = objConexion?.prepareStatement("insert into Tickets (uuid, titulo, descripcion, correo, autor, fecha, estado) values(?,?,?,?,?,?,?)")!!
                    addTicket.setString(1, UUID.randomUUID().toString())
                    addTicket.setString(2, titulo)
                    addTicket.setString(3, descripcion)
                    addTicket.setString(4, correo)
                    addTicket.setString(5, autor)
                    addTicket.setString(6, fecha)
                    addTicket.setString(7, activo)
                    addTicket.executeUpdate()

                    runOnUiThread {
                        // limpiar campos al hacer clic
                        txtTitulo.setText("")
                        txtCorreo.setText("")
                        txtDescripcion.setText("")
                        txtAutor.setText("")
                        txtFecha.setText("")
                        cbActivo.setText("")

                    }
                }
            }

        }
    }
}