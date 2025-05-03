package com.mycompany.proyectoestructuradatos.controlador;

import com.mycompany.proyectoestructuradatos.controlador.Util.ArbolBinarioBusqueda;
import com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas.GestorDeVistas;
import com.mycompany.proyectoestructuradatos.controlador.util.Validador;
import com.mycompany.proyectoestructuradatos.modelo.dao.LibrosDao;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Libro;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import com.mycompany.proyectoestructuradatos.sesion.SesionUsuario;
import com.mycompany.proyectoestructuradatos.vista.JFmConsultarLibros;
import com.mycompany.proyectoestructuradatos.vista.JFmMenu;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class ConsultarLibrosController {

    private final JFmConsultarLibros vista;
    private JFrmLogin vistaLogin;
    private JFmMenu vistaMenu;
    private LibrosDao librosDao;
    public boolean bandera = false;
    private ArbolBinarioBusqueda arbolPrestamos = new ArbolBinarioBusqueda();

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
        this(vista, vistaMenu, vistaLogin);
        bandera = listarLibrosPorBusqueda(busqueda);
    }

    private void listarTodosLosLibros() {
        try {
            vista.mostrarBotonDinamico(vista.getBtnPrestarLibro(), true);
            vista.mostrarComponentes(false);
            List<Map<String, Object>> listaDatos = librosDao.consultarLibros();

            if (!listaDatos.isEmpty()) {
                vista.actualizarVisor(listaDatos);
                // Llenar árbol
                for (Map<String, Object> libroMap : listaDatos) {
                    Libro libro = new Libro(
                            (int) (long) libroMap.get("id_libro"),
                            (String) libroMap.get("titulo"),
                            (String) libroMap.get("autor"),
                            (boolean) libroMap.get("disponible")
                    );
                    arbolPrestamos.insertar(libro);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "No Existen libros disponibles.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al consultar libros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean listarLibrosPorBusqueda(String busqueda) {
        try {
            List<Libro> resultados = arbolPrestamos.buscarPorTitulo(busqueda);
            if (!resultados.isEmpty()) {
                List<Map<String, Object>> datos = new ArrayList<>();
                for (Libro libro : resultados) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id_libro", libro.getId());
                    row.put("titulo", libro.getTitulo());
                    row.put("autor", libro.getAutor());
                    row.put("disponible", libro.isDisponible());
                    datos.add(row);
                }
                vista.mostrarComponentes(true);
                vista.mostrarBotonDinamico(vista.getBtnPrestarLibro(), false);
                vista.actualizarVisor(datos);
                return true;
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontraron libros con ese título.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al buscar libros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

//    public void prestarLibro() {
//        if (vista.getFechaDevolucion().trim().isEmpty() || vista.getIdLibro().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(vista, "Los campos deben estar diligenciados.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String fechaIngresada = vista.getFechaDevolucion().trim();
//        if (!Validador.validarFecha(fechaIngresada)) {
//            JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato DD/MM/AAAA.");
//            return;
//        }
//
//        try {
//            Date fechaPrestamo = Validador.convertirStringADate(fechaIngresada);
//            int idLibro = Integer.parseInt(vista.getIdLibro());
//            
////            Map<String, Object> librosEncontrados = (Map<String, Object>) arbolPrestamos.buscarPorId(idLibro); 
//            List<Map<String, Object>> librosEncontrados = (List<Map<String, Object>>) arbolPrestamos.buscarPorId(idLibro);
//
//            if (!librosEncontrados.isEmpty() && (boolean) librosEncontrados.get(0).get("disponible")) {
//                Usuario usuarioActual = SesionUsuario.getInstancia().getUsuario();
//
//                boolean libroActualizado = librosDao.actualizarDisponibilidadLibro(idLibro, false, usuarioActual, fechaPrestamo);
//
//                if (libroActualizado) {
//                    JOptionPane.showMessageDialog(vista, "Libro prestado exitosamente.");
//                    volverMenu();
//                } else {
//                    JOptionPane.showMessageDialog(vista, "Error al prestar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
//                    volverMenu();
//                }
//            } else {
//                JOptionPane.showMessageDialog(vista, "El libro no está disponible.", "Error", JOptionPane.WARNING_MESSAGE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(vista, "Error al procesar el préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    public void prestarLibro() {
        if (vista.getFechaDevolucion().trim().equals("") || vista.getIdLibro().trim().equals("")) {
            JOptionPane.showMessageDialog(vista, "Los campos deben estar diligenciados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fechaIngresada = vista.getFechaDevolucion().trim();

        if (!Validador.validarFecha(fechaIngresada)) {
            JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato DD/MM/AAAA.");
            return;
        }

        Date fechaPrestamo = Validador.convertirStringADate(fechaIngresada);
        int idLibro = Integer.parseInt(vista.getIdLibro());

        //  Buscar el libro en el árbol en memoria (usando clase Libro)
        Libro libro = arbolPrestamos.buscarPorId(idLibro);

        if (libro == null) {
            JOptionPane.showMessageDialog(vista, "El libro no existe en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!libro.isDisponible()) {
            JOptionPane.showMessageDialog(vista, "El libro no está disponible.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamar al DAO para actualizar base de datos
        Usuario usuarioActual = SesionUsuario.getInstancia().getUsuario();
        boolean libroActualizado = librosDao.actualizarDisponibilidadLibro(idLibro, false, usuarioActual, fechaPrestamo);

        if (libroActualizado) {
            // Actualizar el libro en el árbol también
            libro.setDisponible(false);
            arbolPrestamos.actualizarLibro(libro);  // eliminar e insertar el libro actualizado

            JOptionPane.showMessageDialog(vista, "Libro prestado exitosamente.");
            volverMenu();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al prestar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void volverMenu() {
        GestorDeVistas.mostrarVista(vista, vistaMenu);
    }

    public void volverALogin() {
        GestorDeVistas.mostrarVista(vista, vistaLogin);
    }
}
