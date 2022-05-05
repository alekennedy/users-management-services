package com.alekennedy.usersmanagement.session;

import com.alekennedy.usersmanagement.conexion.AbstractSession;
import com.alekennedy.usersmanagement.entities.NivelAcceso;


/**
 *
 * @author ciac-alejandro
 */
public class NivelAccesoSession extends AbstractSession<NivelAcceso>{

    public NivelAccesoSession(Class<NivelAcceso> entityClass) {
        super(entityClass);
    }
 
    
}
