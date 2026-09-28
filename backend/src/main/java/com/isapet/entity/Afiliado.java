package com.isapet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "afiliado")
public class Afiliado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_identificacion", nullable = false, unique = true, length = 30)
    private String numeroIdentificacion;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 20)
    private String celular;

    @Column(length = 254)
    private String correo;

    @Column(length = 200)
    private String direccion;

    @OneToMany(mappedBy = "afiliado")
    private List<Mascota> mascotas = new ArrayList<>();

    protected Afiliado() {
    }

    public Afiliado(String numeroIdentificacion, String nombres, String apellidos) {
        this(numeroIdentificacion, nombres, apellidos, null, null, null);
    }

    public Afiliado(
            String numeroIdentificacion,
            String nombres,
            String apellidos,
            String celular,
            String correo,
            String direccion) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void actualizarDatosContacto(String nombres, String apellidos, String celular, String correo, String direccion) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;
        this.direccion = direccion;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
        mascota.setAfiliado(this);
    }
}