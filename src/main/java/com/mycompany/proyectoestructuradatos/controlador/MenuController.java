package com.mycompany.proyectoestructuradatos.controlador;


import com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas.GestorDeVistas;
import com.mycompany.proyectoestructuradatos.vista.JFmConsultarLibros;
import com.mycompany.proyectoestructuradatos.vista.JFmConsultarPrestamos;
import com.mycompany.proyectoestructuradatos.vista.JFmMenu;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import java.time.LocalDate;
import javax.swing.JOptionPane;


public class MenuController  {

    private final JFmMenu vista;
    private JFrmLogin vistaLogin;

    public MenuController(JFmMenu vista, JFrmLogin vistaLogin) {
        this.vista = vista;
        this.vistaLogin = vistaLogin; 
        this.vista.getBtnAceptar().addActionListener(e -> realizarAccion());
        this.vista.getBtnSalir().addActionListener(e -> volverALogin());

    }

 
    public void realizarAccion() {
        try {

            int opcion = Integer.parseInt(vista.getOpcion());

            switch (opcion) {
                case 1:
                    vista.limpiarcampos();
                    JFmConsultarLibros vistaLibros = new JFmConsultarLibros();
                    ConsultarLibrosController controladorLibros = new ConsultarLibrosController(vistaLibros,vista, vistaLogin);
                    GestorDeVistas.mostrarVista(vista, vistaLibros);
                    break;
                case 2:
                   String nombreLibro = JOptionPane.showInputDialog(null, "Ingrese El nombre del libro que desea prestar:");
                    vista.limpiarcampos();
                    JFmConsultarLibros vistaLibros2 = new JFmConsultarLibros();
                    ConsultarLibrosController controladorLibros2 = new ConsultarLibrosController(vistaLibros2,vista, vistaLogin, nombreLibro);
                    if(controladorLibros2.bandera){
                    GestorDeVistas.mostrarVista(vista, vistaLibros2);
                    }
                   break;
                case 3:
                    vista.limpiarcampos();
                    JFmConsultarPrestamos vistaPrestamos = new JFmConsultarPrestamos();
                    ConsultarPrestamosController controladorPrestamos = new ConsultarPrestamosController(vistaPrestamos,vista, vistaLogin);
                    GestorDeVistas.mostrarVista(vista, vistaPrestamos);
                    break;
                case 4:
                    vista.limpiarcampos();
                    JFmConsultarPrestamos vistaPrestamos2 = new JFmConsultarPrestamos();
                    ConsultarPrestamosController controladorPrestamos2 = new ConsultarPrestamosController(vistaPrestamos2,vista, vistaLogin, "");
                    GestorDeVistas.mostrarVista(vista, vistaPrestamos2);
                    break;
                default:
                    JOptionPane.showMessageDialog(vista, "Opción no válida.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error en el modulo administrativo.  "
                    + e.getMessage(), "Error administrativo", JOptionPane.ERROR_MESSAGE);
        }

    }



    public void volverALogin() {
        try {         

        GestorDeVistas.mostrarVista(vista, vistaLogin);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar salir de la vista ."
                    + e.getMessage(), "Error vista", JOptionPane.ERROR_MESSAGE);
        }
    }
}
