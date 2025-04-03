package com.mycompany.proyectoestructuradatos.modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {

    // Variables finales para mejor seguridad
    private final String usuario = "postgres";
    private final String contrasenia = "A";
    private final String bd = "Biblioteca";
    private final String ip = "localhost";
    private final String puerto = "5432";

    private final String cadena = "jdbc:postgresql://" + ip + ":" + puerto + "/" + bd;
    private static Connection conexion = null;

    // Constructor privado para Singleton
    private Conexion() {
        try {
            // Cargar el driver
            Class.forName("org.postgresql.Driver");

            // Crear conexión
            conexion = DriverManager.getConnection(cadena, usuario, contrasenia);
            System.out.println("¡Conexión exitosa!");

        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el controlador JDBC. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    // Patrón Singleton para obtener una única conexión
    public static Connection getConnection() {
        if (conexion == null) {
            new Conexion(); // Crear instancia si no existe
        }
        return conexion;
    }
}
