package Conexion

data class Tickets(
    val uuid:String,
    var titulo:String,
    val descripcion:String,
    val autor:String,
    val correo:String,
    val fecha:String,
    val activo:String
)
