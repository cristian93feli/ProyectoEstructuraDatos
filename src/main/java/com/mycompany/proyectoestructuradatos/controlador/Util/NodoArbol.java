/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.proyectoestructuradatos.controlador.Util;

import com.mycompany.proyectoestructuradatos.modelo.entidad.Libro;

public class NodoArbol {
    public Libro libro;
    public NodoArbol izquierda, derecha;

    public NodoArbol(Libro libro) {
        this.libro = libro;
        this.izquierda = null;
        this.derecha = null;
    }
}