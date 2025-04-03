package com.mycompany.proyectoestructuradatos.controlador.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validador {

    public static boolean validarFecha(String fecha) {
        // Expresión regular para validar el formato DD/MM/AAAA
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        if (!fecha.matches(regex)) {
            return false; // Si no cumple con el formato, retorna false
        }

        // Intentar convertir la fecha para verificar si es válida
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false); // Evita aceptar fechas inválidas como 30/02/2023

        try {
            Date date = formato.parse(fecha);
            return true; // Si la conversión es exitosa, la fecha es válida
        } catch (ParseException e) {
            return false; // Si hay error en el parseo, la fecha no es válida
        }
    }

    public static Date convertirStringADate(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false); // Para evitar fechas inválidas como 30/02/2023

        try {
            return formato.parse(fecha); // Retorna la fecha convertida
        } catch (ParseException e) {
            System.out.println("Error: Formato de fecha inválido.");
            return null; // Retorna null si hay error en la conversión
        }
    }
}
