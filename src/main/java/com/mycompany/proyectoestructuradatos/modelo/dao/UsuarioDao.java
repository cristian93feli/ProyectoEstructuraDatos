package com.mycompany.proyectoestructuradatos.modelo.dao;

//import modelo.entidad.Usuario;
//import modelo.conexion.Conexion;
import com.mycompany.proyectoestructuradatos.modelo.conexion.Conexion;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDao {

    private final Connection conexion;

    public UsuarioDao(Connection conexion) {
        this.conexion = Conexion.getConnection();

    }
    
    public UsuarioDao() {
        this.conexion = Conexion.getConnection();

    }

    // Método para validar usuario
    public Usuario obtenerUsuario(String username, String password) {
        Usuario usuario = null;

        String query = "SELECT u.numero_documento, u.usuario, u.password,  u.id_rol  "
                + "FROM usuario as u "
                + "WHERE u.usuario = ? AND u.password = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { 
                usuario = new Usuario(
                        rs.getString("usuario"),
                        rs.getString("password")
                );
                
                usuario.setIdRol(rs.getInt("id_rol"));
                usuario.setNumeroDocumento(rs.getInt("numero_documento"));
            }
        } catch (SQLException e) {
        }
        return usuario;
    }
    
    public Boolean crearUsuario(Usuario usuario) {
        String queryPersona = "INSERT INTO usuario (numero_documento, usuario, password, id_rol, nombre) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            // Guardar usuario
            try (PreparedStatement ps = conexion.prepareStatement(queryPersona)) {
                
                ps.setInt(1, usuario.getNumeroDocumento()); 
                ps.setString(2, usuario.getUsername());           
                ps.setString(3, usuario.getPassword());         
                ps.setInt(4, usuario.getIdRol());     
                ps.setString(5, usuario.getNombre());           

                ps.executeUpdate();
            }

            return true;  
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        }
    }
}

