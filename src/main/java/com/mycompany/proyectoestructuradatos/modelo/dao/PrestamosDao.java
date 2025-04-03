package com.mycompany.proyectoestructuradatos.modelo.dao;

import com.mycompany.proyectoestructuradatos.modelo.conexion.Conexion;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrestamosDao {

    private final Connection conexion;

    public PrestamosDao() {
        this.conexion = Conexion.getConnection();
    }

    public List<Map<String, Object>> consultarprestamos(int numeroDocumento) {
        List<Map<String, Object>> listaLibros = new ArrayList<>();
        String query = "SELECT p.id_prestamo, l.titulo, p.fecha_prestamo, u.nombre\n" +
"	FROM prestamos as p " +
"	inner join libros as l " +
"	on p.id_libro = l.id_libro " +
"	inner join usuario u " +
"	on u.numero_documento = p.id_usuario "+
"       where p.id_usuario = ? AND estado_prestamo = TRUE";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, numeroDocumento);
//            ps.setDate(2, java.sql.Date.valueOf(fechaFinal));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id_prestamo", rs.getLong("id_prestamo"));
                row.put("titulo", rs.getString("titulo"));
                row.put("fecha_prestamo", rs.getDate("fecha_prestamo"));
                row.put("nombre", rs.getString("nombre"));
                listaLibros.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLibros;
    }

    public List<Map<String, Object>> consultarLibrosPorBusqueda(String busqueda) {
        List<Map<String, Object>> listaLibros = new ArrayList<>();
        String query = "SELECT id_libro, titulo, autor, disponible "
                + "FROM public.libros "
                + "WHERE titulo ILIKE ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, "%" + busqueda + "%");
//            ps.setDate(2, java.sql.Date.valueOf(fechaFinal));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id_libro", rs.getLong("id_libro"));
                row.put("titulo", rs.getString("titulo"));
                row.put("autor", rs.getString("autor"));
                row.put("disponible", rs.getBoolean("disponible"));
                listaLibros.add(row);
            }
        } catch (SQLException e) {
        }
        return listaLibros;
    }

    public boolean actualizarDisponibilidadLibro(int idLibro, boolean disponible, Usuario usuario, Date fechaPrestamo) {
        String updateQuery = "UPDATE public.libros SET disponible = ? WHERE id_libro = ?";
        String insertQuery = "INSERT INTO public.\"prestamos\"(id_libro, id_usuario, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado_prestamo) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement psUpdate = conexion.prepareStatement(updateQuery);
                PreparedStatement psInsert = conexion.prepareStatement(insertQuery)) {

            // 1. Actualizar la disponibilidad del libro
            psUpdate.setBoolean(1, disponible);
            psUpdate.setInt(2, idLibro);
            int filasActualizadas = psUpdate.executeUpdate();

            if (filasActualizadas > 0) {

//                Date fechaUtil = new Date(); // java.util.Date
                java.sql.Date fechaSql = new java.sql.Date(fechaPrestamo.getTime()); // Conversión
//                psInsert.setDate(3, fechaSql);
                // 2. Insertar el préstamo en la tabla "Prestamos"
                psInsert.setInt(1, idLibro);
                psInsert.setInt(2, usuario.getNumeroDocumento());
                psInsert.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Fecha actual como fecha de préstamo
                psInsert.setDate(4, fechaSql); // Fecha esperada de devolución 
                psInsert.setNull(5, java.sql.Types.DATE); // Fecha real de devolución aún no establecida
                psInsert.setBoolean(6, true);

                psInsert.executeUpdate();
            }

            return filasActualizadas > 0; // Devuelve true si la actualización fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> consultarPrestamoPorId(int idPrestamo) {
        List<Map<String, Object>> libro = new ArrayList<>();
        String query = "SELECT id_libro FROM public.prestamos WHERE id_prestamo = ? AND estado_prestamo = true";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idPrestamo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id_libro", rs.getInt("id_libro"));
                libro.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libro; // Devuelve una lista (vacía si no encuentra el libro)
    }

    public boolean devolucionPrestamo(int id_prestamo, int idLibro) {
        String updateLibroQuery = "UPDATE public.libros SET disponible = TRUE WHERE id_libro = ?";
        String updatePrestamoQuery = "UPDATE public.prestamos SET estado_prestamo = FALSE , fecha_devolucion_real = ? WHERE id_prestamo = ? ";

        try (PreparedStatement psUpdateLibro = conexion.prepareStatement(updateLibroQuery);
                PreparedStatement psUpdatePrestamo = conexion.prepareStatement(updatePrestamoQuery)) {

            // 1. Actualizar la disponibilidad del libro
            psUpdateLibro.setInt(1, idLibro);
            int filasLibro = psUpdateLibro.executeUpdate();

            // 2. Actualizar el estado del préstamo y la fecha de devolución real
            java.sql.Date fechaDevolucion = new java.sql.Date(System.currentTimeMillis());
            psUpdatePrestamo.setDate(1, fechaDevolucion);
            psUpdatePrestamo.setInt(2, id_prestamo);
            int filasPrestamo = psUpdatePrestamo.executeUpdate();

            // Devuelve true si al menos una de las actualizaciones fue exitosa
            return filasLibro > 0 && filasPrestamo > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
