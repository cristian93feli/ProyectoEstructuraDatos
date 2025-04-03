package com.mycompany.proyectoestructuradatos.controlador;

import com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas.GestorDeVistas;
import com.mycompany.proyectoestructuradatos.modelo.dao.UsuarioDao;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import com.mycompany.proyectoestructuradatos.vista.JFmCrearUsuario;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import javax.swing.JOptionPane;


public class CrearUsuarioController {

    private JFmCrearUsuario vista;
    private JFrmLogin vistaLogin;
    private UsuarioDao usuarioDao;

    public CrearUsuarioController(JFmCrearUsuario vista, JFrmLogin vistaLogin) {
        this.usuarioDao = new UsuarioDao();
        this.vista = vista;
        this.vistaLogin = vistaLogin; 
        this.vista.getBtnAceptar().addActionListener(e -> realizarAccion());
        this.vista.getBtnSalir().addActionListener(e -> volverALogin());
    }

    public void realizarAccion() {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(vista.getNombre());
            usuario.setNumeroDocumento(Integer.parseInt(vista.getDocumento()));
            usuario.setUsername(vista.getUsuario());
            usuario.setPassword(vista.getContrasena());
            usuario.setIdRol(1);



            boolean insercionCorrecta = usuarioDao.crearUsuario(usuario);

            if (insercionCorrecta) {
                JOptionPane.showMessageDialog(vista, "Cliente creado con exito.");
                volverALogin();

                
            } else {
                JOptionPane.showMessageDialog(vista, "Error creando el cliente.",
                        "Error al crear cliente", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar crear cliente. "
                    + e.getMessage(), "Error crear cliente", JOptionPane.ERROR_MESSAGE);

        }
    }

    
    public void volverALogin() {
        try {

            GestorDeVistas.mostrarVista(vista, vistaLogin);
        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar salir de la vista. "
                    + e.getMessage(), "Error vista", JOptionPane.ERROR_MESSAGE);

        }
    }

}
