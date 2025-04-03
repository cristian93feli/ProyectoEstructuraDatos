package com.mycompany.proyectoestructuradatos.controlador;

import com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas.GestorDeVistas;
import com.mycompany.proyectoestructuradatos.controlador.util.Validador;
import com.mycompany.proyectoestructuradatos.modelo.dao.LibrosDao;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import com.mycompany.proyectoestructuradatos.sesion.SesionUsuario;
import com.mycompany.proyectoestructuradatos.vista.JFmConsultarLibros;
import com.mycompany.proyectoestructuradatos.vista.JFmMenu;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class ConsultarLibrosController {

    private final JFmConsultarLibros vista;
    private JFrmLogin vistaLogin;
    private JFmMenu vistaMenu;
    private LibrosDao librosDao;
    public boolean bandera = false;

    public ConsultarLibrosController(JFmConsultarLibros vista, JFmMenu vistaMenu, JFrmLogin vistaLogin) {
        this.librosDao = new LibrosDao();
        this.vista = vista;
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        listarTodosLosLibros();
        this.vista.getBtnCerrarSesion().addActionListener(e -> volverALogin());
        this.vista.getBtnVolverMenu().addActionListener(e -> volverMenu());
        this.vista.getBtnGenerarPrestamo().addActionListener(e -> prestarLibro());
        this.vista.getBtnPrestarLibro().addActionListener(e -> vista.mostrarComponentes(true));
    }

    public ConsultarLibrosController(JFmConsultarLibros vista, JFmMenu vistaMenu, JFrmLogin vistaLogin, String busqueda) {
        this.librosDao = new LibrosDao();
        this.vista = vista;
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        bandera = listarLibrosPorBusqueda(busqueda);
        this.vista.getBtnCerrarSesion().addActionListener(e -> volverALogin());
        this.vista.getBtnVolverMenu().addActionListener(e -> volverMenu());
        this.vista.getBtnGenerarPrestamo().addActionListener(e -> prestarLibro());
    }

    private void listarTodosLosLibros() {
        try {
            vista.mostrarBotonDinamico(vista.getBtnPrestarLibro(), true);
            vista.mostrarComponentes(false);
            List<Map<String, Object>> listaDatos = librosDao.consultarLibros();

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

    private boolean listarLibrosPorBusqueda(String busqueda) {
        try {

            List<Map<String, Object>> listaDatos = librosDao.consultarLibrosPorBusqueda(busqueda);

            if (!listaDatos.isEmpty()) {
                vista.mostrarComponentes(true);
                vista.mostrarBotonDinamico(vista.getBtnPrestarLibro(), false);
                vista.actualizarVisor(listaDatos);
                // volverALogin();
                return true;
            } else {
                JOptionPane.showMessageDialog(vista, "No Existen datos para el nombre del libro ingresado.");

            }
        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar consultar la atraccion. "
                    + e.getMessage(), "Error vista", JOptionPane.ERROR_MESSAGE);

        }
        return false;
    }

public void prestarLibro() {
    if (vista.getFechaDevolucion().trim().equals("") || vista.getIdLibro().trim().equals("")) {
        JOptionPane.showMessageDialog(vista, "Los campos deben estar diligenciados.", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        String fechaIngresada = vista.getFechaDevolucion().trim();
        if (Validador.validarFecha(fechaIngresada)) {
            Date fechaPrestamo = Validador.convertirStringADate(fechaIngresada);
            int idLibro = Integer.parseInt(vista.getIdLibro());
            
            // Verificar si el libro está disponible antes de prestarlo
            List<Map<String, Object>> librosEncontrados = librosDao.consultarLibroPorId(idLibro);
            
            if (!librosEncontrados.isEmpty() && (boolean) librosEncontrados.get(0).get("disponible")) {
                Usuario usuarioActual = SesionUsuario.getInstancia().getUsuario();
                
                boolean Libroactualizado = librosDao.actualizarDisponibilidadLibro(idLibro, false, usuarioActual, fechaPrestamo);
                
                if (Libroactualizado) {
                    JOptionPane.showMessageDialog(vista, "Libro prestado exitosamente.");
                    volverMenu();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al prestar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
                    volverMenu();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "El libro no está disponible.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato DD/MM/AAAA.");
        }
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
