package com.tfgLeilaFraileSimon.sprinta.modelo;

// el trabajador que ha entrado (lo que me devuelve el login)
public class Trabajador {
    private Integer id;
    private String nombreCompleto;
    private String email;
    private String rol;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
