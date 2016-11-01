package com.intelit.json.syncUsers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyncUser {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("idAsesor")
    private String idAsesor;
    @JsonProperty("usuario")
    private String usuario;
    @JsonProperty("password")
    private String password;
    @JsonProperty("administrador")
    private Integer administrador;
    @JsonProperty("activo")
    private Integer activo;
    @JsonProperty("fechaExpiracion")
    private String fechaExpiracion;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The idAsesor
     */
    public String getIdAsesor() {
        return idAsesor;
    }

    /**
     *
     * @param idAsesor
     * The idAsesor
     */
    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    /**
     *
     * @return
     * The usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     * The usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The administrador
     */
    public Integer getAdministrador() {
        return administrador;
    }

    /**
     *
     * @param administrador
     * The administrador
     */
    public void setAdministrador(Integer administrador) {
        this.administrador = administrador;
    }

    /**
     *
     * @return
     * The activo
     */
    public Integer getActivo() {
        return activo;
    }

    /**
     *
     * @param activo
     * The activo
     */
    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    /**
     *
     * @return
     * The fechaExpiracion
     */
    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     *
     * @param fechaExpiracion
     * The fechaExpiracion
     */
    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}