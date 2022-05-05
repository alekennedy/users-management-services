package com.alekennedy.usersmanagement.session;

import com.alekennedy.usersmanagement.conexion.AbstractSession;
import com.alekennedy.usersmanagement.entities.GrupoUsuario;



/**
 *
 * @author alejandro
 */
public class GrupoUsuarioSession extends AbstractSession<GrupoUsuario>{

    public GrupoUsuarioSession(Class<GrupoUsuario> entityClass) {
        super(entityClass);
    }
}
