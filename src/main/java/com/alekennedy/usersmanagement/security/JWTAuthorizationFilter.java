/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alekennedy.usersmanagement.security;

import com.alekennedy.usersmanagement.etc.SystemVariables;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import redis.clients.jedis.Jedis;

/**
 *
 * @author nucie
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
    
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                    throws IOException, ServletException {
            String header = req.getHeader(Constantes.HEADER_AUTHORIZACION_KEY);
            Logger.getLogger(JWTAuthorizationFilter.class.getCanonicalName()).log(Level.INFO, "HEADER: "+header);
            if (header == null || !header.startsWith(Constantes.TOKEN_BEARER_PREFIX)) {
                    chain.doFilter(req, res);
                    return;
            }
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
            String token = request.getHeader(Constantes.HEADER_AUTHORIZACION_KEY);
            Logger.getLogger(JWTAuthorizationFilter.class.getCanonicalName()).log(Level.INFO, "token: "+token);
            if (token != null) {
                    // Se procesa el token y se recupera el usuario.
                    Jedis jedis = new Jedis(SystemVariables.getDataBaseRedisIp());
                    String key = jedis.get(token);
                    Logger.getLogger(JWTAuthorizationFilter.class.getCanonicalName()).log(Level.INFO, "key: "+key);
                    String user = JWT.require(Algorithm.HMAC256(key)).build()
                            .verify(token.replace(Constantes.TOKEN_BEARER_PREFIX, ""))
                            .getSubject();

                    if (user != null) {
                            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    }
                    return null;
            }
            return null;
    }
    
    
}
