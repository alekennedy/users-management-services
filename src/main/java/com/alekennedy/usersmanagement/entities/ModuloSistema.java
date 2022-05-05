package com.alekennedy.usersmanagement.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ciac-alejandro
 */
@Entity
@Table(name="modulo_sistema", schema = "sysadmin")
public class ModuloSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ms_id")
    private Long msId;
    
    @Column(name="ms_modulo")
    private String msModulo;
    
    @Column(name = "ms_icono")
    private String msIcono;
    
    @OneToMany(cascade = CascadeType.DETACH)
    @JoinColumn(name="ms_nivelacceso")
    private List<NivelAcceso> msNivelacceso;
    
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "modulo_sistema_grupo", joinColumns = @JoinColumn(name="ms_id"), inverseJoinColumns = @JoinColumn(name="gu_id"))
    private List<GrupoUsuario> grupoUsuario;
    
    @Column(name = "ms_configuracion")
    private boolean msConfiguracion;

    
    public ModuloSistema() {
        grupoUsuario = new ArrayList<>();
        msNivelacceso = new ArrayList<>();
    }

    public Long getMsId() {
        return msId;
    }

    public void setMsId(Long msId) {
        this.msId = msId;
    }

    public String getMsModulo() {
        return msModulo;
    }

    public void setMsModulo(String msModulo) {
        this.msModulo = msModulo;
    }

    public String getMsIcono() {
        return msIcono;
    }

    public void setMsIcono(String msIcono) {
        this.msIcono = msIcono;
    }

    public List<NivelAcceso> getMsNivelacceso() {
        return msNivelacceso;
    }

    public void setMsNivelacceso(List<NivelAcceso> msNivelacceso) {
        this.msNivelacceso = msNivelacceso;
    }

    public List<GrupoUsuario> getGrupoUsuario() {
        return grupoUsuario;
    }

    public void setGrupoUsuario(List<GrupoUsuario> grupoUsuario) {
        this.grupoUsuario = grupoUsuario;
    }

    public boolean isMsConfiguracion() {
        return msConfiguracion;
    }

    public void setMsConfiguracion(boolean msConfiguracion) {
        this.msConfiguracion = msConfiguracion;
    }

    
}
