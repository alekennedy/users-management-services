package com.alekennedy.usersmanagement.entities;

import java.io.Serializable;
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
import javax.persistence.Table;

/**
 *
 * @author alejandro
 */
@Entity
@Table(name = "grupo_usuario", schema = "sysadmin")
public class GrupoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gu_id")
    private Long guId;
    
    @Column(name = "gu_denominacion")
    private String guDenominacion;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "nivel_acceso_grupo", 
            joinColumns = {@JoinColumn(name="gu_id")}, 
            inverseJoinColumns = {@JoinColumn(name="na_id")}) 
    private List<NivelAcceso> guNivelGrupo;

    public Long getGuId() {
        return guId;
    }

    public void setGuId(Long guId) {
        this.guId = guId;
    }

    public String getGuDenominacion() {
        return guDenominacion;
    }

    public void setGuDenominacion(String guDenominacion) {
        this.guDenominacion = guDenominacion;
    }

    public List<NivelAcceso> getGuNivelGrupo() {
        return guNivelGrupo;
    }

    public void setGuNivelGrupo(List<NivelAcceso> guNivel_grupo) {
        this.guNivelGrupo = guNivel_grupo;
    }
}
