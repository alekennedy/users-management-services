
package com.alekennedy.usersmanagement.session;

import com.alekennedy.usersmanagement.conexion.AbstractSession;
import com.alekennedy.usersmanagement.entities.ModuloSistema;
import com.alekennedy.usersmanagement.entities.NivelAcceso;
import com.alekennedy.usersmanagement.entities.NivelAccesoUsuario;
import com.alekennedy.usersmanagement.entities.UserAuthentication;
import com.alekennedy.usersmanagement.entities.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

/**
 *
 * @author ale
 */
public class NivelAccesoUsuarioSession extends AbstractSession<NivelAccesoUsuario>{
    
    public NivelAccesoUsuarioSession(Class<NivelAccesoUsuario> entityClass) {
        super(entityClass);
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
            }
        }catch(HibernateException e){
            manejarExcepcion(e);
        }finally{
            closeSession();
        }
        
        return ua;
    }
    
    public List<ModuloSistema> getModulosSistema(Long idUser)throws HibernateException{
        iniciarOperacion();
        Query query = this.getSession().createSQLQuery("SELECT na_id, na_descripcion, na_icono,\n" +
            "na_menu, na_ruta, ms_nivelacceso, ms_id, ms_configuracion, ms_icono, ms_modulo, nau_crear, "+
            "nau_editar, nau_anular, nau_movimiento\n" +
            "FROM sysadmin.nivel_acceso_usuario, sysadmin.nivel_acceso, sysadmin.modulo_sistema\n" +
            "WHERE nau_acceso = na_id and ms_id = nau_modulo and ms_configuracion = false and nau_usuario = :userId\n"+
            "ORDER BY ms_id");
        query.setParameter("userId", idUser);
        List<Object[]> result = query.list();
        return getModulos(result);
        
    }    
    
    public List<ModuloSistema> getConfiguracionesSistema(Long idUser)throws HibernateException{
        Query query = this.getSession().createSQLQuery("SELECT na_id, na_descripcion, na_icono,\n" +
            "na_menu, na_ruta, ms_nivelacceso, ms_id, ms_configuracion, ms_icono, ms_modulo, nau_crear, \n"+
            "nau_editar, nau_anular, nau_movimiento \n" +
            "FROM sysadmin.nivel_acceso_usuario, sysadmin.nivel_acceso, sysadmin.modulo_sistema\n" +
            "WHERE nau_acceso = na_id and ms_id = nau_modulo and ms_configuracion = true and nau_usuario = :userId\n"+
            "ORDER BY ms_id");
        
        query.setParameter("userId", idUser);
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
                ms.setMsId(Long.parseLong(objects[6].toString()));
                ms.setMsModulo((String) objects[9]);
                ms.setMsIcono(objects[8].toString());
                sw = 1;
            }
            
            
            if(ms.getMsId()==Long.parseLong(objects[6].toString())){
                NivelAcceso na = new NivelAcceso();
                na.setNaId(Long.parseLong(objects[0].toString()));
                na.setNaDescripcion((String) objects[1]);
                na.setNaMenu((String) objects[3]);
                na.setNaRuta((String) objects[4]);
                na.setNaIcono(objects[2].toString());
                na.setNaCrear((Boolean) objects[10]);
                na.setNaEditar((Boolean) objects[11]);
                na.setNaAnular((Boolean) objects[12]);
                na.setNaMovimiento((Boolean) objects[13]);
                ms.getMsNivelacceso().add(na);
            }else{
                modulos.add(ms);
                
                ms = new ModuloSistema();
                ms.setGrupoUsuario(null);
                ms.setMsId(Long.parseLong(objects[6].toString()));
                ms.setMsModulo((String) objects[9]);
                ms.setMsIcono(objects[8].toString());
                
                NivelAcceso na = new NivelAcceso();
                na.setNaId(Long.parseLong(objects[0].toString()));
                na.setNaDescripcion((String) objects[1]);
                na.setNaMenu((String) objects[3]);
                na.setNaRuta((String) objects[4]);
                na.setNaIcono(objects[2].toString());
                na.setNaCrear((Boolean) objects[10]);
                na.setNaEditar((Boolean) objects[11]);
                na.setNaAnular((Boolean) objects[12]);
                na.setNaMovimiento((Boolean) objects[13]);
                ms.getMsNivelacceso().add(na);
            }            
        }
        
        modulos.add(ms);        
        return modulos;
    }
    
}
