package com.intelit.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by SISTEMA on 22/08/2016.
 */
@DatabaseTable
public class Usuario {
    public static final String ID = "_id";
    public static final String NOMBRE = "nombre";
    public static final String USUARIO = "usuario";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String ESTATUS = "estatus";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NOMBRE)
    private String nombre;
    @DatabaseField(columnName = USUARIO)
    private String usuario;
    @DatabaseField(columnName = PASSWORD)
    private String password;
    @DatabaseField(columnName = EMAIL)
    private String email;
    @DatabaseField(columnName = ESTATUS)
    private int estatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
