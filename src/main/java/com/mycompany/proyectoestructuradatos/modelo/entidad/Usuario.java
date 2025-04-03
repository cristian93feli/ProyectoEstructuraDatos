package com.mycompany.proyectoestructuradatos.modelo.entidad;

public class Usuario {
    private String username;
    private String password;
    private int numeroDocumento;
    private int idRol;
    private String nombre;

 
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Usuario() {
    }
    
    


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

 
    
    
}
