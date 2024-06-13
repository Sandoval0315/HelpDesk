package sandoval0315.helpdesk

import Conexion.Conexion
import Conexion.Tickets
import RecyclerView.Adaptador
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtDescripcion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCrearTicket = findViewById<Button>(R.id.btnCrearTicket)
        val rcvTickets=findViewById<RecyclerView>(R.id.rcvTickets)

        btnCrearTicket.setOnClickListener {
            val intent = Intent(this, TicketNew::class.java)
            startActivity(intent)
        }

        rcvTickets.layoutManager= LinearLayoutManager(this)


        fun obtenerDatos():List<Tickets>{
            val objConexion= Conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet=statement?.executeQuery("select * from Tickets")!!


            val tickets = mutableListOf<Tickets>()
            while (resultSet.next()){
                val uuid=resultSet.getString("uuid")
                val titulo = resultSet.getString("titulo")
                val descripcion = resultSet.getString("descripcion")
                val autor = resultSet.getString("autor")
                val correo = resultSet.getString("correo")
                val fecha = resultSet.getString("fecha")
                val activo = resultSet.getString("estado")
                val ticket = Tickets(uuid,titulo,descripcion,autor,correo,fecha,activo)
                tickets.add(ticket)
            }
            return tickets
        }

        CoroutineScope ( Dispatchers.IO) .launch {
            val productosBd=obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdapter = Adaptador(productosBd)
                rcvTickets.adapter=miAdapter
            }
        }
    }
}