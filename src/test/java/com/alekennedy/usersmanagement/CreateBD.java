/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alekennedy.usersmanagement;

import com.alekennedy.usersmanagement.entities.NivelAcceso;
import com.alekennedy.usersmanagement.session.NivelAccesoSession;

/**
 *
 * @author ale
 */
public class CreateBD {
    
    public static void main(String[] arg){
        
        NivelAcceso na = new NivelAcceso();
        na.setNaIcono("ic");
        na.setNaMenu("m");
        na.setNaRuta("/");
        NivelAccesoSession nas = new NivelAccesoSession(NivelAcceso.class);
        nas.guardar(na);
    }
    
}
