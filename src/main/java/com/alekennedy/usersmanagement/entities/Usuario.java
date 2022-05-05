package com.alekennedy.usersmanagement.entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author alejandro
 */
@Entity
@Table(name = "usuario", schema = "sysadmin")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;
    
    @Column(name = "user_nombre")
    private String userNombre;
    
    @Column(name = "user_alias", unique = true)
    private String userAlias;
    
    @Column(name = "user_password")
    private String userPassword;
    
    @Column(name = "user_cargo")
    private String userCargo;
    
    @Column(name = "user_email", unique = true)
    private String userEmail;
    
    @JoinColumn(name = "user_grupo")
    @OneToOne()
    private GrupoUsuario userGrupo;
    
    @Column(name = "user_online")
    private Boolean userOnline;
    
    @Column(name = "user_avatar", length = 1000000)
    private String userAvatar;
    
    @Column(name = "user_token")
    private String userToken;
    
    @Column(name= "user_activo")
    private Boolean userActivo;
    
    @Column(name = "user_custon_access")
    private Boolean userCustonAccess;
    
    @Transient
    private List<NivelAccesoUsuario> userCustonAccessLevels;

    public Usuario() {
        userCustonAccessLevels = new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNombre() {
        return userNombre;
    }

    public void setUserNombre(String userNombre) {
        this.userNombre = userNombre;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserCargo() {
        return userCargo;
    }

    public void setUserCargo(String userCargo) {
        this.userCargo = userCargo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public GrupoUsuario getUserGrupo() {
        return userGrupo;
    }

    public void setUserGrupo(GrupoUsuario userGrupo) {
        this.userGrupo = userGrupo;
    }

    public Boolean getUserOnline() {
        return userOnline;
    }

    public void setUserOnline(Boolean userOnline) {
        this.userOnline = userOnline;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Boolean getUserActivo() {
        return userActivo;
    }

    public void setUserActivo(Boolean userActivo) {
        this.userActivo = userActivo;
    }

    public Boolean getUserCustonAccess() {
        return userCustonAccess;
    }

    public void setUserCustonAccess(Boolean userCustonAccess) {
        this.userCustonAccess = userCustonAccess;
    }

    public List<NivelAccesoUsuario> getUserCustonAccessLevels() {
        return userCustonAccessLevels;
    }

    public void setUserCustonAccessLevels(List<NivelAccesoUsuario> userCustonAccessLevels) {
        this.userCustonAccessLevels = userCustonAccessLevels;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }
    
    public String hash(String value, String algorithm){
        try {
            MessageDigest message = MessageDigest.getInstance(algorithm);
            byte[] md5Bytes = message.digest(value.getBytes());
            
            StringBuilder cadena = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                cadena.append(Integer.toHexString(md5Bytes[i] & 0xFF | 0x100).substring(1,3));
            }
            
            return cadena.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    }
    
    
    
}
