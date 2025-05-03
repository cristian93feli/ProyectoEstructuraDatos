package com.mycompany.proyectoestructuradatos.controlador.Util;

import com.mycompany.proyectoestructuradatos.modelo.entidad.Libro;
import java.util.ArrayList;
import java.util.List;

public class ArbolBinarioBusqueda {

    private NodoArbol raiz;

    public void insertar(Libro libro) {
        raiz = insertarRec(raiz, libro);
    }

    private NodoArbol insertarRec(NodoArbol nodo, Libro libro) {
        if (nodo == null) {
            return new NodoArbol(libro);
        }
        if (libro.compareTo(nodo.libro) < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, libro);
        } else {
            nodo.derecha = insertarRec(nodo.derecha, libro);
        }
        return nodo;
    }

    public List<Libro> buscarPorTitulo(String busqueda) {
        List<Libro> resultado = new ArrayList<>();
        buscarRec(raiz, busqueda.toLowerCase(), resultado);
        return resultado;
    }

    private void buscarRec(NodoArbol nodo, String busqueda, List<Libro> resultado) {
        if (nodo == null) return;

        if (nodo.libro.getTitulo().toLowerCase().contains(busqueda)) {
            resultado.add(nodo.libro);
        }

        buscarRec(nodo.izquierda, busqueda, resultado);
        buscarRec(nodo.derecha, busqueda, resultado);
    }

    // ✅ NUEVO: Buscar por ID
    public Libro buscarPorId(int idLibro) {
        return buscarPorIdRec(raiz, idLibro);
    }

    private Libro buscarPorIdRec(NodoArbol nodo, int idLibro) {
        if (nodo == null) return null;

        if (nodo.libro.getId()== idLibro) {
            return nodo.libro;
        }

        Libro encontradoIzq = buscarPorIdRec(nodo.izquierda, idLibro);
        if (encontradoIzq != null) return encontradoIzq;

        return buscarPorIdRec(nodo.derecha, idLibro);
    }

 
    public void actualizarLibro(Libro libroActualizado) {
        eliminar(libroActualizado.getTitulo());
        insertar(libroActualizado);
    }

    
    public void eliminar(String titulo) {
        raiz = eliminarRec(raiz, titulo.toLowerCase());
    }

    private NodoArbol eliminarRec(NodoArbol nodo, String titulo) {
        if (nodo == null) return null;

        String tituloNodo = nodo.libro.getTitulo().toLowerCase();

        if (titulo.compareTo(tituloNodo) < 0) {
            nodo.izquierda = eliminarRec(nodo.izquierda, titulo);
        } else if (titulo.compareTo(tituloNodo) > 0) {
            nodo.derecha = eliminarRec(nodo.derecha, titulo);
        } else {
            // Nodo encontrado
            if (nodo.izquierda == null) return nodo.derecha;
            if (nodo.derecha == null) return nodo.izquierda;

            NodoArbol sucesor = encontrarMin(nodo.derecha);
            nodo.libro = sucesor.libro;
            nodo.derecha = eliminarRec(nodo.derecha, sucesor.libro.getTitulo().toLowerCase());
        }

        return nodo;
    }

    private NodoArbol encontrarMin(NodoArbol nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }
        return nodo;
    }
}
