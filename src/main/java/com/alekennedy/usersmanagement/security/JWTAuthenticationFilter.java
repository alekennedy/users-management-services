/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alekennedy.usersmanagement.security;

import com.alekennedy.usersmanagement.entities.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 *
 * @author nucie
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {        
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Logger.getLogger(JWTAuthenticationFilter.class.getCanonicalName()).log(Level.INFO, "attemptAuthentication");
            
            Usuario credenciales = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            Logger.getLogger(JWTAuthenticationFilter.class.getCanonicalName()).log(Level.INFO,"creds: "+credenciales.getUserAlias()+" "+ credenciales.getUserPassword());
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            credenciales.getUserAlias(), credenciales.getUserPassword(), new ArrayList<>()));
        } catch (IOException e) {
                Logger.getLogger(JWTAuthenticationFilter.class.getCanonicalName()).log(Level.SEVERE,"ERROR EN attemptAuthentication");
                throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = JWT.create().withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Constantes.TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(Constantes.SUPER_SECRET_KEY));
                
        
        Logger.getLogger(JWTAuthenticationFilter.class.getCanonicalName()).log(Level.INFO, "token: "+token);
        
        response.addHeader(Constantes.HEADER_AUTHORIZACION_KEY, Constantes.TOKEN_BEARER_PREFIX + " " + token);
    }
    
    
}
