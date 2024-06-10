package Conexion

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class Conexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@192.168.0.21:1521:xe"
            val usuario = "system"
            val contraseña = "sandoval"

            val conection = DriverManager.getConnection(url, usuario, contraseña)
            return conection
        }
        catch (e: Exception){
            println("ERROR : $e")
            return null
        }
    }
}