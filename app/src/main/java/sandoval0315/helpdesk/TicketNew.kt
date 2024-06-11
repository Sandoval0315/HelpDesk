package sandoval0315.helpdesk

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
    }
}