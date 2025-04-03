package com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas;

import javax.swing.JFrame;

public class GestorDeVistas {

    // Método para cambiar entre vistas
    public static void mostrarVista(JFrame vistaActual, JFrame nuevaVista) {
        vistaActual.dispose(); // Cierra la vista actual
        nuevaVista.setVisible(true); // Muestra la nueva vista
    }
}
