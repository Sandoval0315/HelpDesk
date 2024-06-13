package RecyclerView

import Conexion.Conexion
import Conexion.Tickets
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sandoval0315.helpdesk.R


class Adaptador (private var Datos: List<Tickets>):RecyclerView.Adapter<ViewHolder>(){

 fun actualizarTicket(nuevaLista:List<Tickets>){
  Datos=nuevaLista
  notifyDataSetChanged()
 }

 fun actualizarTicketDespuesDeActualizarTickets(uuid: String,nombreTicket:String){
  val index=Datos.indexOfFirst { it.uuid==uuid }
  Datos[index].titulo=nombreTicket
  notifyItemChanged(index)
 }

 fun eliminarTicket(nombreTicket:String,position: Int){
  val listaTickets = Datos .toMutableList()
  listaTickets.removeAt(position)

  GlobalScope.launch(Dispatchers.IO) {

   val objConexion= Conexion().cadenaConexion()

   val deleteTicket = objConexion?.prepareStatement("delete Tickets where Titulo=?")!!
   deleteTicket.setString( 1,nombreTicket)
   deleteTicket.executeUpdate()

   val commit = objConexion.prepareStatement( "commit")!!
   commit.executeUpdate()
  }
  Datos=listaTickets.toList()
  notifyItemRemoved(position)
  notifyDataSetChanged()

 }

 fun actualizarTicket(nombreTicket: String , uuid:String){
  GlobalScope.launch(Dispatchers.IO){

   val objConexion = Conexion().cadenaConexion()

   val updateProducto = objConexion?.prepareStatement("update Tickets set Titulo = ? where uuid = ?")!!

   updateProducto.setString(1,nombreTicket)
   updateProducto.setString(2,uuid)
   updateProducto.executeUpdate()

   val commit = objConexion.prepareStatement("commit")
   commit.executeUpdate()

   withContext(Dispatchers.Main){
    actualizarTicketDespuesDeActualizarTickets(uuid,nombreTicket  )
   }
  }

 }

 override fun onBindViewHolder(holder: ViewHolder, position: Int) {
  val ticket = Datos[position]
  holder.textView.text = ticket.titulo

  val item =Datos[position]


  holder.imgBorrar.setOnClickListener {

   val context = holder.itemView.context
   val builder = AlertDialog.Builder(context)

   builder.setTitle("¿estas seguro?")

   builder.setMessage("Deseas en verdad eliminar el registro")

   builder.setPositiveButton("si"){dialog,wich ->
    eliminarTicket(item.titulo,position)
   }

   builder.setNegativeButton("no"){dialog,wich ->

   }

   val alertDialog=builder.create()

   alertDialog.show()

  }

  holder.imgEditar.setOnClickListener {
   val context=holder.itemView.context

   val builder = AlertDialog.Builder(context)
   builder.setTitle("Editar nombre")

   val cuadroTicket = EditText(context)
   cuadroTicket.setHint(item.titulo)
   builder.setView(cuadroTicket)

   builder.setPositiveButton("Actualizar"){
     dialog,which->actualizarTicket(cuadroTicket.text.toString(),item.uuid)
   }

   builder.setNegativeButton("cancelar"){
     dialog,which->dialog.dismiss()
   }
   val dialog = builder.create()
   dialog.show()
  }

  holder.itemView.setOnClickListener {
   //invoco el contexto
   val context = holder.itemView.context
  }

 }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
   val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
   return ViewHolder(vista)
  }

 override fun getItemCount() = Datos.size



 }