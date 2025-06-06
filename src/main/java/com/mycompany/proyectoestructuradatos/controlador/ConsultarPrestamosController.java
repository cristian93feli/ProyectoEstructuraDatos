package com.mycompany.proyectoestructuradatos.controlador;

import com.mycompany.proyectoestructuradatos.controlador.Util.ArbolBinarioBusqueda;
import com.mycompany.proyectoestructuradatos.controlador.Util.GrafoPrestamos;
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
    private GrafoPrestamos grafoPrestamos = new GrafoPrestamos();
   

    public ConsultarPrestamosController(JFmConsultarPrestamos vista, JFmMenu vistaMenu, JFrmLogin vistaLogin) {
        this.prestamosDao = new PrestamosDao();
        this.vista = vista;
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        this.grafoPrestamos = new GrafoPrestamos();
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
            List<Map<String, Object>> listaDatos = prestamosDao.consultarprestamos(usuarioActual.getNumeroDocumento());

            // Limpiar grafo y agregar nodos/aristas actualizados
            grafoPrestamos.limpiar();
            for (Map<String, Object> prestamo : listaDatos) {
                int idPrestamo = ((Long) prestamo.get("id_prestamo")).intValue();
                String nombreUsuario = usuarioActual.getNombre();
                String tituloLibro = (String) prestamo.get("titulo");

                grafoPrestamos.agregarRelacion(nombreUsuario, tituloLibro);
            }

            vista.mostrarBotonDinamico(vista.getBtnDevolverLibro(), true);
            vista.mostrarComponentes(false);
            vista.actualizarVisor(listaDatos);

            if (listaDatos.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No Existen libros disponibles.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al consultar los préstamos. "
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        if (vista.getIdPrestamo().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar un ID de préstamo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idPrestamo = Integer.parseInt(vista.getIdPrestamo());
            List<Map<String, Object>> prestamosEncontrados = prestamosDao.consultarPrestamoPorId(idPrestamo);

            if (!prestamosEncontrados.isEmpty()) {
                int idLibro = Integer.parseInt(prestamosEncontrados.get(0).get("id_libro").toString());

                boolean exito = prestamosDao.devolucionPrestamo(idPrestamo, idLibro);

                if (exito) {
                    // Eliminar relación del grafo
                    String tituloLibro = buscarTituloLibroPorId(idLibro);
                    String nombreUsuario = SesionUsuario.getInstancia().getUsuario().getNombre();
                    grafoPrestamos.eliminarRelacion(nombreUsuario, tituloLibro);

                    JOptionPane.showMessageDialog(vista, "Libro devuelto exitosamente.");
                    volverMenu();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al devolver el libro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "El préstamo ingresado no existe.", "Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "ID de préstamo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   
       private String buscarTituloLibroPorId(int idLibro) {
        List<Map<String, Object>> listaLibros = prestamosDao.consultarLibrosPorBusqueda("");
        for (Map<String, Object> libro : listaLibros) {
            if (((Long) libro.get("id_libro")).intValue() == idLibro) {
                return (String) libro.get("titulo");
            }
        }
        return "";
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
