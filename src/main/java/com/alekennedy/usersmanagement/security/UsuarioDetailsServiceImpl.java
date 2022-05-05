package com.alekennedy.usersmanagement.security;

import static java.util.Collections.emptyList;

import com.alekennedy.usersmanagement.entities.Usuario;
import com.alekennedy.usersmanagement.session.UsuarioSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author nucie
 */
@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService{
    
    private UsuarioSession userSession;

    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        userSession = new UsuarioSession(Usuario.class);
        Logger.getLogger(UsuarioDetailsServiceImpl.class.getCanonicalName()).log(Level.INFO, "USER: "+userName);
        Usuario user = userSession.getUsuario(userName);
        if(user == null){
            throw new UsernameNotFoundException(userName);
        }
        return new User(user.getUserAlias(), user.getUserPassword(), emptyList());
    }    
    
    
}
