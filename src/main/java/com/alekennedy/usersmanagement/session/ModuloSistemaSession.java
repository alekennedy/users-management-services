package com.alekennedy.usersmanagement.session;

import com.alekennedy.usersmanagement.conexion.AbstractSession;
import com.alekennedy.usersmanagement.entities.ModuloSistema;
import com.alekennedy.usersmanagement.entities.NivelAcceso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author ciac-alejandro
 */
public class ModuloSistemaSession extends AbstractSession<ModuloSistema>{

    public ModuloSistemaSession(Class<ModuloSistema> entityClass) {
        super(entityClass);
    }
    
    public List<ModuloSistema> getNivelesAccesos(){
        List<ModuloSistema> modulos = new ArrayList<>();
        try {
            iniciarOperacion();
            String sql = "SELECT ms_id, ms_configuracion, ms_icono, ms_modulo, na_id, na_descripcion, \n" +
            "	na_icono, na_menu, na_ruta, ms_nivelacceso\n" +
            "	FROM sysadmin.modulo_sistema, sysadmin.nivel_acceso\n" +
            "	WHERE ms_id = ms_nivelacceso ORDER BY ms_id;";
            Query query = getSession().createSQLQuery(sql);
            List<Object[]> resultados = query.getResultList();
            boolean swFist = true;
            ModuloSistema ms = new ModuloSistema();
            Long id = null;
            for (Object[] resultado : resultados) {
                if(swFist){
                    ms = new ModuloSistema();
                    ms.setMsId(Long.parseLong(resultado[0].toString()));
                    ms.setMsConfiguracion(Boolean.parseBoolean(resultado[1].toString()));
                    ms.setMsIcono(resultado[2].toString());
                    ms.setMsModulo(resultado[3].toString());
                    id = ms.getMsId();
                    swFist = false;
                }
                
                if(id == Long.parseLong(resultado[0].toString())){
                    NivelAcceso na = new NivelAcceso();
                    na.setNaId(Long.parseLong(resultado[4].toString()));
                    na.setNaDescripcion(resultado[5].toString());
                    na.setNaIcono(resultado[6].toString());
                    na.setNaMenu(resultado[7].toString());
                    na.setNaRuta(resultado[8].toString());
                    ms.getMsNivelacceso().add(na);
                }else{
                    modulos.add(ms);
                    ms = new ModuloSistema();
                    ms.setMsId(Long.parseLong(resultado[0].toString()));
                    ms.setMsConfiguracion(Boolean.parseBoolean(resultado[1].toString()));
                    ms.setMsIcono(resultado[2].toString());
                    ms.setMsModulo(resultado[3].toString());
                    id = ms.getMsId();
                    
                    NivelAcceso na = new NivelAcceso();
                    na.setNaId(Long.parseLong(resultado[4].toString()));
                    na.setNaDescripcion(resultado[5].toString());
                    na.setNaIcono(resultado[6].toString());
                    na.setNaMenu(resultado[7].toString());
                    na.setNaRuta(resultado[8].toString());
                    ms.getMsNivelacceso().add(na);
                }
            }
            modulos.add(ms);
        } catch (Exception e) {
        }
        return modulos;
    }
    
}
