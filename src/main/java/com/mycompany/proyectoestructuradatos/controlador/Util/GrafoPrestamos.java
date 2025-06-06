/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.proyectoestructuradatos.controlador.Util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author crist
 */
public class GrafoPrestamos {
    private final Map<String, Set<String>> grafo;

    public GrafoPrestamos() {
        grafo = new HashMap<>();
    }

    // Agrega relación usuario -> libro
    public void agregarRelacion(String usuario, String libro) {
        grafo.putIfAbsent(usuario, new HashSet<>());
        grafo.get(usuario).add(libro);
    }

    // Elimina la relación usuario -> libro
    public void eliminarRelacion(String usuario, String libro) {
        if (grafo.containsKey(usuario)) {
            grafo.get(usuario).remove(libro);
            if (grafo.get(usuario).isEmpty()) {
                grafo.remove(usuario);
            }
        }
    }

    // Obtiene todos los libros que ha tomado un usuario
    public Set<String> obtenerLibrosPorUsuario(String usuario) {
        return grafo.getOrDefault(usuario, Collections.emptySet());
    }

    // Verifica si existe una relación directa
    public boolean existeRelacion(String usuario, String libro) {
        return grafo.containsKey(usuario) && grafo.get(usuario).contains(libro);
    }

    // Limpia todo el grafo
    public void limpiar() {
        grafo.clear();
    }

    // Para depurar o imprimir conexiones
    public void imprimirGrafo() {
        for (Map.Entry<String, Set<String>> entry : grafo.entrySet()) {
            System.out.println("Usuario: " + entry.getKey() + " -> Libros: " + entry.getValue());
        }
    }
}
