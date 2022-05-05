package com.alekennedy.usersmanagement.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ciac-alejandro
 */
@Entity

@Table(name = "nivel_acceso", schema = "sysadmin")
public class NivelAcceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="na_id")
    private Long naId;

    @Column(name = "na_descripcion")
    private String naDescripcion;
    
    @Column(name = "na_ruta")
    private String naRuta;
    
    @Column(name = "na_menu")
    private String naMenu;
    
    @Column(name = "na_icono")
    private String naIcono;
    
    @Transient
    private Boolean naCrear;
    
    @Transient
    private Boolean naEditar;
    
    @Transient
    private Boolean naAnular;
    
    @Transient
    private Boolean naMovimiento;

    public NivelAcceso() {
        //nathing to do
        this.setNaAnular(true);
        this.setNaCrear(true);
        this.setNaEditar(true);
        this.setNaMovimiento(true);
        
    }

    public Long getNaId() {
        return naId;
    }

    public void setNaId(Long naId) {
        this.naId = naId;
    }

    public String getNaDescripcion() {
        return naDescripcion;
    }

    public void setNaDescripcion(String naDescripcion) {
        this.naDescripcion = naDescripcion;
    }

    public String getNaRuta() {
        return naRuta;
    }

    public void setNaRuta(String naRuta) {
        this.naRuta = naRuta;
    }

    public String getNaMenu() {
        return naMenu;
    }

    public void setNaMenu(String naMenu) {
        this.naMenu = naMenu;
    }

    public String getNaIcono() {
        return naIcono;
    }

    public void setNaIcono(String naIconos) {
        this.naIcono = naIconos;
    }

    public Boolean getNaCrear() {
        return naCrear;
    }

    public void setNaCrear(Boolean naCrear) {
        this.naCrear = naCrear;
    }

    public Boolean getNaEditar() {
        return naEditar;
    }

    public void setNaEditar(Boolean naEditar) {
        this.naEditar = naEditar;
    }

    public Boolean getNaAnular() {
        return naAnular;
    }

    public void setNaAnular(Boolean naAnular) {
        this.naAnular = naAnular;
    }

    public Boolean getNaMovimiento() {
        return naMovimiento;
    }

    public void setNaMovimiento(Boolean naMovimiento) {
        this.naMovimiento = naMovimiento;
    }

    
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (naId != null ? naId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelAcceso)) {
            return false;
        }
        NivelAcceso other = (NivelAcceso) object;
        if ((this.naId == null && other.naId != null) || (this.naId != null && !this.naId.equals(other.naId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NivelAcceso{" + "na_id=" + naId + ", na_descripcion=" + naDescripcion + ", na_ruta=" + naRuta + ", na_menu=" + naMenu + '}';
    }

    
    
}
