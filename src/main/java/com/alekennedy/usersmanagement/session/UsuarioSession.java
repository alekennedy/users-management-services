package com.alekennedy.usersmanagement.session;

import com.alekennedy.usersmanagement.conexion.AbstractSession;
import com.alekennedy.usersmanagement.entities.ModuloSistema;
import com.alekennedy.usersmanagement.entities.NivelAcceso;
import com.alekennedy.usersmanagement.entities.NivelAccesoUsuario;
import com.alekennedy.usersmanagement.entities.UserAuthentication;
import com.alekennedy.usersmanagement.entities.Usuario;
import com.alekennedy.usersmanagement.etc.Configuration;
import com.alekennedy.usersmanagement.etc.SystemVariables;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import redis.clients.jedis.Jedis;

/**
 *
 * @author ciac-alejandro
 */
public class UsuarioSession extends AbstractSession<Usuario>{

    public UsuarioSession(Class<Usuario> entityClass) {
        super(entityClass);
    }
  
    
    
    public Usuario getUsuario(String alias)throws HibernateException{
        Usuario user = new Usuario();
        try {
            iniciarOperacion();
            Query query = this.getSession().createQuery("from Usuario u where u.userAlias = :alias");
            query.setParameter("alias", alias);
            user = (Usuario) query.uniqueResult();            
            
        } catch (HibernateException e) {
            manejarExcepcion(e);
        }finally{
            closeSession();
        }
        
        return user;
    }
    
    public Usuario getUsuarioByToken(String token)throws HibernateException{
        Usuario user = new Usuario();
        try {
            iniciarOperacion();
            Query query = this.getSession().createQuery("from Usuario u where u.userToken = :token");
            query.setParameter("token", token);
            user = (Usuario) query.uniqueResult();            
            
        } catch (HibernateException e) {
            manejarExcepcion(e);
        }finally{
            closeSession();
        }
        
        return user;
    }
    
    public List<Usuario> getUsuarios(String nombre)throws HibernateException{
        List<Usuario> users = new ArrayList<>();
        try {
            iniciarOperacion();
            Query query = this.getSession().createQuery("from Usuario u where upper(u.userNombre) like '%"+nombre.toUpperCase()+"%'");            
            users = query.list();
        } catch (HibernateException e) {
            manejarExcepcion(e);
        }finally{
            closeSession();
        }
        return users;
    }
    
    public UserAuthentication autenticacionUsuario(String alias, String pass)throws Exception{
        UserAuthentication ua = new UserAuthentication();
        ua.setAuth(false);
        try{
            iniciarOperacion();            
            Query query = 
            this.getSession().createQuery("from Usuario u where u.userAlias = :alias and u.userPassword = :pass");
            query.setParameter("alias", alias);
            query.setParameter("pass", pass);
            
            Usuario user = (Usuario) query.uniqueResult();            
            if(user!=null){
                if(user.getUserCustonAccess()==false){
                    if(user.getUserAlias().equals(alias) && user.getUserPassword().equals(pass)){

                        ua.setAuth(true);
                        ua.setAccesos(getModulosSistema(user.getUserGrupo().getGuId()));                    
                        ua.setConfiguraciones(getConfiguracionesSistema(user.getUserGrupo().getGuId()));
                        ua.setToken(user.getUserToken());
                        ua.setUserId(user.getUserId());
                        ua.setAlias(user.getUserAlias());
                        ua.setNombre(user.getUserNombre());
                        ua.setCargo(user.getUserCargo());
                        ua.setImagen(user.getUserAvatar());
                    }
                }else{
                    NivelAccesoUsuarioSession naus = new NivelAccesoUsuarioSession(NivelAccesoUsuario.class);
                    if(user.getUserAlias().equals(alias) && user.getUserPassword().equals(pass)){

                        ua.setAuth(true);
                        ua.setAccesos(naus.getModulosSistema(user.getUserId()));                    
                        ua.setConfiguraciones(naus.getConfiguracionesSistema(user.getUserId()));
                        ua.setToken(user.getUserToken());
                        ua.setUserId(user.getUserId());
                        ua.setAlias(user.getUserAlias());
                        ua.setNombre(user.getUserNombre());
                        ua.setCargo(user.getUserCargo());
                        ua.setImagen(user.getUserAvatar());
                    }
                    
                }
            }
        }catch(HibernateException e){
            manejarExcepcion(e);
        }finally{
            closeSession();
        }
        
        return ua;
    }
    
    public List<ModuloSistema> getModulosSistema(Long idGroup)throws HibernateException{
        
        
        Query query = this.getSession().createSQLQuery("SELECT ms.ms_id as idModulo, "+
                    "ms.ms_modulo as moduloName, na.na_id as nivelId," +
                    "na.na_descripcion as nivelDescripcion, na.na_menu as nivelMenu, "+
                    "na.na_ruta as nivelRuta, na.na_icono as naIcom, ms.ms_icono as msIcom "+
                    "from sysadmin.modulo_sistema as ms, "+
                    "sysadmin.grupo_usuario as gu, modulo_sistema_grupo msg, " +
                    "sysadmin.nivel_acceso na, nivel_acceso_grupo nag "+
                    "where msg.ms_id = ms.ms_id and gu.gu_id = :idGroup and " +
                    "msg.gu_id = gu.gu_id and " +
                    "na.na_id = nag.na_id and " +
                    "nag.gu_id = gu.gu_id and "+
                    "ms_configuracion = false and " +
                    "na.ms_nivelacceso = ms.ms_id order by idModulo");
        query.setParameter("idGroup", idGroup);
        List<Object[]> result = query.list();
        return getModulos(result);
        
    }    
    
    public List<ModuloSistema> getConfiguracionesSistema(Long idGroup)throws HibernateException{
        
        Query query = this.getSession().createSQLQuery("SELECT ms.ms_id as idModulo, "+
                    "ms.ms_modulo as moduloName, na.na_id as nivelId," +
                    "na.na_descripcion as nivelDescripcion, na.na_menu as nivelMenu, "+
                    "na.na_ruta as nivelRuta, na.na_icono as naIcom, ms.ms_icono as msIcom "+
                    "from sysadmin.modulo_sistema as ms, "+
                    "sysadmin.grupo_usuario as gu, modulo_sistema_grupo msg, " +
                    "sysadmin.nivel_acceso na, nivel_acceso_grupo nag "+
                    "where msg.ms_id = ms.ms_id and gu.gu_id = :idGroup and " +
                    "msg.gu_id = gu.gu_id and " +
                    "na.na_id = nag.na_id and " +
                    "nag.gu_id = gu.gu_id and "+
                    "ms_configuracion = true and " +
                    "na.ms_nivelacceso = ms.ms_id order by idModulo");
        
        query.setParameter("idGroup", idGroup);
        List<Object[]> result = query.list();
        return getModulos(result);
    }  
    
    
    private List<ModuloSistema> getModulos(List<Object[]> result){
        List<ModuloSistema> modulos = new ArrayList<>();
        int sw = 0;
        ModuloSistema ms = null;
        
        for (Object[] objects : result) {
            if(sw == 0){
                ms = new ModuloSistema();
                ms.setGrupoUsuario(null);
                ms.setMsId(Long.parseLong(objects[0].toString()));
                ms.setMsModulo((String) objects[1]);
                ms.setMsIcono(objects[7].toString());
                sw = 1;
            }
            
            
            if(ms.getMsId()==Long.parseLong(objects[0].toString())){
                NivelAcceso na = new NivelAcceso();
                na.setNaId(Long.parseLong(objects[2].toString()));
                na.setNaDescripcion((String) objects[3]);
                na.setNaMenu((String) objects[4]);
                na.setNaRuta((String) objects[5]);
                na.setNaIcono(objects[6].toString());
                ms.getMsNivelacceso().add(na);
            }else{
                modulos.add(ms);
                
                ms = new ModuloSistema();
                ms.setGrupoUsuario(null);
                ms.setMsId(Long.parseLong(objects[0].toString()));
                ms.setMsModulo((String) objects[1]);
                ms.setMsIcono(objects[7].toString());
                
                NivelAcceso na = new NivelAcceso();
                na.setNaId(Long.parseLong(objects[2].toString()));
                na.setNaDescripcion((String) objects[3]);
                na.setNaMenu((String) objects[4]);
                na.setNaRuta((String) objects[5]);                
                na.setNaIcono(objects[6].toString());
                ms.getMsNivelacceso().add(na);                
            }            
        }
        
        modulos.add(ms);        
        return modulos;
    }
    
    public String getUserToken(String email){
        String token = null;
        try {
            Usuario user = new Usuario();
            iniciarOperacion();
            Query query = this.getSession().createQuery("from Usuario u where u.userEmail = :email");  
            query.setParameter("email", email);
            
            user = (Usuario) query.uniqueResult();
            token = user.getUserToken();
        } catch (HibernateException e) {
            manejarExcepcion(e);
        }finally{
            closeSession();
        }
        return token;
    }
    
    public Long guardar(Usuario usuario) throws HibernateException{
        Long id = 0l;
        try {
            iniciarOperacion();            
            id = (Long)getSession().save(usuario);
            if(usuario.getUserCustonAccessLevels().size()>0){
                for(NivelAccesoUsuario nau :usuario.getUserCustonAccessLevels()){
                    nau.setNauUser(usuario);
                    getSession().save(nau);
                }
            }
            getTx().commit();
        } catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
        return id;
    }
    
    public void actualizar(Usuario usuario) throws HibernateException{
        try {
            iniciarOperacion();
            getSession().update(usuario);
            if(usuario.getUserCustonAccessLevels().size()>0){
                for(NivelAccesoUsuario nau :usuario.getUserCustonAccessLevels()){
                    nau.setNauUser(usuario);
                    if(nau.getLevelActive()){
                        getSession().saveOrUpdate(nau);
                    }else{
                        getSession().delete(nau);
                    }
                    
                }
            }
            getTx().commit();
        } catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
    }
    
    public void logout(String token){
        Jedis jedis = new Jedis(SystemVariables.getDataBaseRedisIp());
        jedis.del(token);
        
    }
}
