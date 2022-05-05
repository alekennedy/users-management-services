
package com.alekennedy.usersmanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ale
 */
@Entity
@Table(name = "nivel_acceso_usuario", schema = "sysadmin")
public class NivelAccesoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nau_id")
    private Long nauId;
    
    @OneToOne
    @JoinColumn(name = "nau_acceso")
    private NivelAcceso nauAcceso;
    
    @OneToOne
    @JoinColumn(name = "nau_usuario")
    private Usuario nauUser;
    
    @OneToOne
    @JoinColumn(name = "nau_modulo")
    private ModuloSistema nauModulo;
    
    @Column(name = "nau_crear")
    private Boolean nauCrear;
    
    @Column(name = "nau_editar")
    private Boolean nauEditar;
    
    @Column(name = "nau_anular")
    private Boolean nauAnular;
    
    @Column(name = "nau_movimiento")
    private Boolean nauMovimiento;
    
    @Transient
    private Boolean levelActive;

    public NivelAccesoUsuario() {
    }

    public Long getNauId() {
        return nauId;
    }

    public void setNauId(Long nauId) {
        this.nauId = nauId;
    }

    public NivelAcceso getNauAcceso() {
        return nauAcceso;
    }

    public void setNauAcceso(NivelAcceso nauAcceso) {
        this.nauAcceso = nauAcceso;
    }

    public Usuario getNauUser() {
        return nauUser;
    }

    public void setNauUser(Usuario nauUser) {
        this.nauUser = nauUser;
    }

    public ModuloSistema getNauModulo() {
        return nauModulo;
    }

    public void setNauModulo(ModuloSistema nauModulo) {
        this.nauModulo = nauModulo;
    }

    public Boolean getLevelActive() {
        return levelActive;
    }

    public void setLevelActive(Boolean levelActive) {
        this.levelActive = levelActive;
    }

    public Boolean getNauCrear() {
        return nauCrear;
    }

    public void setNauCrear(Boolean nauCrear) {
        this.nauCrear = nauCrear;
    }

    public Boolean getNauEditar() {
        return nauEditar;
    }

    public void setNauEditar(Boolean nauEditar) {
        this.nauEditar = nauEditar;
    }

    public Boolean getNauAnular() {
        return nauAnular;
    }

    public void setNauAnular(Boolean nauAnular) {
        this.nauAnular = nauAnular;
    }

    public Boolean getNauMovimiento() {
        return nauMovimiento;
    }

    public void setNauMovimiento(Boolean nauMovimiento) {
        this.nauMovimiento = nauMovimiento;
    }
}
