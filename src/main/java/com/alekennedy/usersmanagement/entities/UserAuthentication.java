package com.alekennedy.usersmanagement.entities;

import java.util.List;



/**
 *
 * @author alejandro
 */
public class UserAuthentication {
    private boolean auth;
    private String authorization;
    private String token;
    private List<ModuloSistema> accesos;
    private List<ModuloSistema> configuraciones;
    private Long userId;
    private String nombre;
    private String alias;
    private String cargo;
    private String imagen;

    

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ModuloSistema> getAccesos() {
        return accesos;
    }

    public void setAccesos(List<ModuloSistema> accesos) {
        this.accesos = accesos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<ModuloSistema> getConfiguraciones() {
        return configuraciones;
    }

    public void setConfiguraciones(List<ModuloSistema> configuraciones) {
        this.configuraciones = configuraciones;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
