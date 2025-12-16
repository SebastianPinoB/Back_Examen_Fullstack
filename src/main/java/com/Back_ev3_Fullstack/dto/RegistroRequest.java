package com.Back_ev3_Fullstack.dto;

public class RegistroRequest {
    private String correo;
    private String contrasenia;
    private String nombreCompleto;
    private String telefono;
    private String fechaNacimiento;
    private Integer edad;
    private Boolean isDuocStudent;
    private Integer descuentoPermanente;
    private String codigoReferido;


    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Boolean getIsDuocStudent() {
        return isDuocStudent;
    }

    public void setIsDuocStudent(Boolean isDuocStudent) {
        this.isDuocStudent = isDuocStudent;
    }

    public Integer getDescuentoPermanente() {
        return descuentoPermanente;
    }

    public void setDescuentoPermanente(Integer descuentoPermanente) {
        this.descuentoPermanente = descuentoPermanente;
    }

    public String getCodigoReferido() {
        return codigoReferido;
    }

    public void setCodigoReferido(String codigoReferido) {
        this.codigoReferido = codigoReferido;
    }
}
