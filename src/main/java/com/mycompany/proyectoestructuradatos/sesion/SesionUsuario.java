
package com.mycompany.proyectoestructuradatos.sesion;

import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;


public class SesionUsuario {
    private static SesionUsuario instancia;
    private Usuario usuario;

    private SesionUsuario() {
        // Constructor privado para evitar múltiples instancias
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void cerrarSesion() {
        usuario = null;
    }
}
