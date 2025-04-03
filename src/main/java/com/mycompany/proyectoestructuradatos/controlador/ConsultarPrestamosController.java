package com.mycompany.proyectoestructuradatos.controlador;

import com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas.GestorDeVistas;
import com.mycompany.proyectoestructuradatos.controlador.util.Validador;
import com.mycompany.proyectoestructuradatos.modelo.dao.LibrosDao;
import com.mycompany.proyectoestructuradatos.modelo.dao.PrestamosDao;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import com.mycompany.proyectoestructuradatos.sesion.SesionUsuario;
import com.mycompany.proyectoestructuradatos.vista.JFmConsultarLibros;
import com.mycompany.proyectoestructuradatos.vista.JFmConsultarPrestamos;
import com.mycompany.proyectoestructuradatos.vista.JFmMenu;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class ConsultarPrestamosController {

    private final JFmConsultarPrestamos vista;
    private JFrmLogin vistaLogin;
    private JFmMenu vistaMenu;
    private PrestamosDao prestamosDao;
    public boolean bandera = false;

    public ConsultarPrestamosController(JFmConsultarPrestamos vista, JFmMenu vistaMenu, JFrmLogin vistaLogin) {
        this.prestamosDao = new PrestamosDao();
        this.vista = vista;
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        listarTodosLosPrestamos();
        this.vista.getBtnCerrarSesion().addActionListener(e -> volverALogin());
        this.vista.getBtnVolverMenu().addActionListener(e -> volverMenu());
        this.vista.getBtnGenerarDevolucion().addActionListener(e -> generarDevolucion());
        this.vista.getBtnDevolverLibro().addActionListener(e -> vista.mostrarComponentes(true));
    }

    public ConsultarPrestamosController(JFmConsultarPrestamos vista, JFmMenu vistaMenu, JFrmLogin vistaLogin, String busqueda) {
        this.prestamosDao = new PrestamosDao();
        this.vista = vista;
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        listarTodosLosPrestamos();
        this.vista.ocultarComponentes(false);
        this.vista.getBtnCerrarSesion().addActionListener(e -> volverALogin());
        this.vista.getBtnVolverMenu().addActionListener(e -> volverMenu());
//        this.vista.getBtnGenerarDevolucion().addActionListener(e -> prestarLibro());
    }

    private void listarTodosLosPrestamos() {
        try {
            

                    
            Usuario usuarioActual = SesionUsuario.getInstancia().getUsuario();
            vista.mostrarBotonDinamico(vista.getBtnDevolverLibro(), true);
            vista.mostrarComponentes(false);
            List<Map<String, Object>> listaDatos = prestamosDao.consultarprestamos(usuarioActual.getNumeroDocumento());

            if (!listaDatos.isEmpty()) {
                vista.actualizarVisor(listaDatos);
                // volverALogin();

            } else {
                JOptionPane.showMessageDialog(vista, "No Existen libros disponibles.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar consultar la atraccion. "
                    + e.getMessage(), "Error vista", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void listarLibrosPorBusqueda(String busqueda) {
        try {

                   
            Usuario usuarioActual = SesionUsuario.getInstancia().getUsuario();
            
            List<Map<String, Object>> listaDatos = prestamosDao.consultarprestamos(usuarioActual.getNumeroDocumento());

            if (!listaDatos.isEmpty()) {
                vista.actualizarVisor(listaDatos);
                // volverALogin();

            } else {
                JOptionPane.showMessageDialog(vista, "No Existen libros disponibles.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar consultar la atraccion. "
                    + e.getMessage(), "Error vista", JOptionPane.ERROR_MESSAGE);

        }
    }

public void generarDevolucion() {
    if (vista.getIdPrestamo().trim().equals("")) {
        JOptionPane.showMessageDialog(vista, "Los campos deben estar diligenciados.", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        
            int idPrestamo = Integer.parseInt(vista.getIdPrestamo());
            
            // Verificar si el libro está disponible antes de prestarlo
            List<Map<String, Object>> prestamosEncontrados = prestamosDao.consultarPrestamoPorId(idPrestamo);
            
            if (!prestamosEncontrados.isEmpty()) {
             
                
                boolean Libroactualizado = prestamosDao.devolucionPrestamo(idPrestamo , Integer.parseInt(prestamosEncontrados.get(0).get("id_libro").toString()));
                
                if (Libroactualizado) {
                    JOptionPane.showMessageDialog(vista, "Libro Devuelto exitosamente.");
                    volverMenu();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al Devolver el libro.", "Error", JOptionPane.ERROR_MESSAGE);
                    volverMenu();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "El prestamo ingresado no existe.", "Error", JOptionPane.WARNING_MESSAGE);
            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato DD/MM/AAAA.");
//        }
    }
}

    public void volverMenu() {
        try {

            GestorDeVistas.mostrarVista(vista, vistaMenu);
        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar salir de la vista ."
                    + e.getMessage(), "Error vista", JOptionPane.ERROR_MESSAGE);

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
