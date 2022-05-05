/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alekennedy.usersmanagement.security;

/**
 *
 * @author nucie
 */
public class Constantes {
    
    public static final String LOGIN_URL = "/users/auth";
    public static final String LOGOUT_URL = "/users/logout";
    public static final String EMAIL_URL = "/users/resetPassword";
    public static final String RESET_URL = "/users/restablecer";
    public static final String RESET2_URL = "/users/restablecer";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT

    public static final String ISSUER_INFO = "https://www.alekennedy.com/";
    public static final String SUPER_SECRET_KEY = "-@12412sdasdaSSFhhhasd?";
    public static final long TOKEN_EXPIRATION_TIME = 864_000_000; // 10 day
    
}
