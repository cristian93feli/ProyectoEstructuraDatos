package com.mycompany.proyectoestructuradatos;

import com.mycompany.proyectoestructuradatos.controlador.LoginController;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import com.mycompany.proyectoestructuradatos.modelo.conexion.Conexion;
import com.mycompany.proyectoestructuradatos.modelo.dao.UsuarioDao;




public class MainProyecto {

    public static void main(String[] args) {

        UsuarioDao usuarioDAO = new UsuarioDao(Conexion.getConnection());
        JFrmLogin vista = new JFrmLogin();

        // Crear controlador
        LoginController login = new LoginController(usuarioDAO, vista);

//        // Mostrar la vista
        vista.setVisible(true);

    }
}
