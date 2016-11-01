package com.intelit.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by SISTEMA on 22/08/2016.
 */
@DatabaseTable
public class Usuario {
    public static final String ID = "_id";
    public static final String ID_ASESOR = "id_asesor";
    public static final String USUARIO = "usuario";
    public static final String PASSWORD = "password";
    public static final String ADMINISTRADOR = "administrador";
    public static final String ACTIVO = "activo";
    public static final String FECHA_EXPIRACION = "fecha_expiracion";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = ID_ASESOR)
    private int id_asesor;
    @DatabaseField(columnName = USUARIO)
    private String usuario;
    @DatabaseField(columnName = PASSWORD)
    private String password;
    @DatabaseField(columnName = ADMINISTRADOR)
    private int administrador;
    @DatabaseField(columnName = ACTIVO)
    private int activo;
    @DatabaseField(columnName = FECHA_EXPIRACION)
    private String fecha_expiracion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_asesor() {
        return id_asesor;
    }

    public void setId_asesor(int id_asesor) {
        this.id_asesor = id_asesor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdministrador() {
        return administrador;
    }

    public void setAdministrador(int administrador) {
        this.administrador = administrador;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getFecha_expiracion() { return fecha_expiracion; }

    public void setFecha_expiracion(String fecha_expiracion) { this.fecha_expiracion = fecha_expiracion; }


}
