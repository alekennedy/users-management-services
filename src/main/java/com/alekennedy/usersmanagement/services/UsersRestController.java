package com.alekennedy.usersmanagement.services;

import com.alekennedy.usersmanagement.entities.ChangeUserCredential;
import com.alekennedy.usersmanagement.entities.GrupoUsuario;
import com.alekennedy.usersmanagement.entities.ModuloSistema;
import com.alekennedy.usersmanagement.entities.NivelAccesoUsuario;
import com.alekennedy.usersmanagement.entities.UserAuthentication;
import com.alekennedy.usersmanagement.entities.Usuario;
import com.alekennedy.usersmanagement.etc.Configuration;
import com.alekennedy.usersmanagement.etc.SystemVariables;
import com.alekennedy.usersmanagement.security.Constantes;
import com.alekennedy.usersmanagement.session.GrupoUsuarioSession;
import com.alekennedy.usersmanagement.session.ModuloSistemaSession;
import com.alekennedy.usersmanagement.session.NivelAccesoUsuarioSession;
import com.alekennedy.usersmanagement.session.UsuarioSession;
import com.alekennedy.usersmanagement.thread.EnviarEmail;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import py.com.lib.util.log.Errors;
import py.com.lib.util.values.Variables;
import redis.clients.jedis.Jedis;
/**
 *
 * @author ale
 */
@RestController

@CrossOrigin(origins = {"*"})
@RequestMapping("/users")
public class UsersRestController {
    UsuarioSession usuariosSession = new UsuarioSession(Usuario.class);
    ModuloSistemaSession msSession = new ModuloSistemaSession(ModuloSistema.class);
    NivelAccesoUsuarioSession nauSession = new NivelAccesoUsuarioSession(NivelAccesoUsuario.class);
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "auth", method = POST, consumes = {MediaType.APPLICATION_JSON})   
   public ResponseEntity<UserAuthentication> authentication(@RequestBody Usuario user){
       UserAuthentication auth = new UserAuthentication();
       try {
           auth = usuariosSession.autenticacionUsuario(user.getUserAlias(), user.hash(user.getUserPassword(),"SHA"));      
           
            HttpHeaders headers = new HttpHeaders();
            if(auth.getToken()!=null){
                String token = JWT.create()
                     .withSubject(auth.getAlias())
                     .withExpiresAt(new Date(System.currentTimeMillis() + Constantes.TOKEN_EXPIRATION_TIME))
                     .sign(Algorithm.HMAC256(auth.getToken()));
                Jedis jedis = new Jedis(SystemVariables.getDataBaseRedisIp());
                jedis.set(Constantes.TOKEN_BEARER_PREFIX+token, auth.getToken());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                jedis.expireAt(Constantes.TOKEN_BEARER_PREFIX+token, cal.getTimeInMillis());
                auth.setAuthorization(Constantes.TOKEN_BEARER_PREFIX + token);
                headers.add(Constantes.HEADER_AUTHORIZACION_KEY, Constantes.TOKEN_BEARER_PREFIX + token);
               
            }
           return new ResponseEntity<>(auth, headers, HttpStatus.OK);
       } catch (Exception ex) {
           Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
           Errors.message("Error rest /auth: "+ex.getMessage());
           return new ResponseEntity<>(auth, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "addUser", method = POST, consumes = {MediaType.APPLICATION_JSON})
   public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario){
       try {
            String password = usuario.getUserPassword();
            if(usuario.getUserCustonAccessLevels().size()>0){
                usuario.setUserCustonAccess(Boolean.TRUE);
            }else{
                usuario.setUserCustonAccess(Boolean.FALSE);
            }
            
            usuario.setUserToken(usuario.hash(usuario.getUserAlias()+usuario.getUserPassword(), "SHA"));
            usuario.setUserPassword(usuario.hash(usuario.getUserPassword(), "SHA"));
            usuario.setUserActivo(Boolean.TRUE);
            usuario.setUserId(usuariosSession.guardar(usuario));
            usuario.setUserCustonAccessLevels(null);
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Hola ").append(usuario.getUserNombre()).append(",\n\n\n");
            mensaje.append("Tu es usuario: ").append(usuario.getUserAlias());
            mensaje.append("\nTu  es contraseña: ").append(password);
            
            EnviarEmail email = new EnviarEmail("Bienvanido al sistema", mensaje.toString(), usuario.getUserEmail());
            Thread sendEmailThread = new Thread(email);
            sendEmailThread.start(); 
            return new ResponseEntity<Usuario>(usuario, HttpStatus.ACCEPTED);    
       }catch(Exception ex){
            String jsonError = "{\"error\":\""+ex.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /addUser: "+ex.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
       }
       
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "editUser", method = POST, consumes = {MediaType.APPLICATION_JSON})
   public ResponseEntity<?> actualizarUsuario(@RequestBody Usuario usuario){
       try {
           Usuario user = new Usuario();
           user = usuariosSession.obtener(usuario.getUserId(), user);
           user.setUserAlias(usuario.getUserAlias());
           user.setUserAvatar(usuario.getUserAvatar());
           user.setUserCargo(usuario.getUserCargo());
           user.setUserEmail(usuario.getUserEmail());
           user.setUserGrupo(usuario.getUserGrupo());
           user.setUserNombre(usuario.getUserNombre());
           user.setUserCustonAccessLevels(usuario.getUserCustonAccessLevels());
           
           usuariosSession.actualizar(user);
           
           StringBuilder mensaje = new StringBuilder();
           mensaje.append("Hola ").append(usuario.getUserNombre()).append(",\n\n\n");
           mensaje.append("Se ha registrado una actualización de datos de tu perfil de usuario");
           
           EnviarEmail email = new EnviarEmail("Actualización de datos del Sistema", mensaje.toString(), usuario.getUserEmail());
           Thread sendEmailThread = new Thread(email);
           sendEmailThread.start(); 
           usuario.setUserCustonAccessLevels(null);
           return new ResponseEntity<>(usuario, HttpStatus.ACCEPTED);
       } catch (Exception e) {
           String jsonError = "{\"error\":\""+e.getMessage()+"\"}";
           Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, e);
           Errors.message("Error rest /editUser: "+e.getMessage());
           return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
       }
        
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "resetPassword", method = GET, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> forgotPass(@RequestParam("email") String email){
        try {
            String token = usuariosSession.getUserToken(email);
            
            if(token!=null){
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("Link para restablecer contraseña: http://localhost:8383/SGC-WEB/index.html#!/restablecerPassword/");
                mensaje.append(token);
                
                EnviarEmail emailSender = new EnviarEmail("Recuperar Contraseña del Sistema", mensaje.toString(), email);
                Thread sendEmailThread = new Thread(emailSender);
                sendEmailThread.start(); 
                
                return new ResponseEntity<>("{\"status\": \"OK\"}", HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>("{\"status\": \"INVALIDO\"}", HttpStatus.ACCEPTED);
            }
            
        } catch (Exception e) {
           String jsonError = "{\"error\":\""+e.getMessage()+"\"}";
           Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, e);
           Errors.message("Error rest /resetPassword: "+e.getMessage());
           return new ResponseEntity<>("{\"status\": \"ERROR\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "list", method = GET, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<?> getUsuarios(@RequestParam("nombre") String nombre){
       try {
           List<Usuario> usuarios = usuariosSession.getUsuarios(nombre);
           for (int i = 0; i < usuarios.size(); i++) {
               usuarios.get(i).getUserGrupo().setGuNivelGrupo(null);
           }
           return new ResponseEntity<>(usuarios, HttpStatus.ACCEPTED);
       } catch (Exception e) {
           String jsonError = "{\"error\":\""+e.getMessage()+"\"}";
           Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, e);
           Errors.message("Error rest /list: "+e.getMessage());
           return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "changePassword", method = POST, consumes = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> cambiarPassword(@RequestBody ChangeUserCredential changePassword){
       Usuario user = new Usuario();
       try {
           
            user = usuariosSession.getUsuario(changePassword.getUserAlias());
            if(user.hash(changePassword.getUserOldpassword(), "SHA").equals(user.getUserPassword())){
                user.setUserPassword(user.hash(changePassword.getUserNewpassword(), "SHA"));
                usuariosSession.actualizar(user);
                return new ResponseEntity<>("{\"changed\":true}", HttpStatus.ACCEPTED);
            }
       }catch (Exception e) {
           Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, e.getMessage(), e.getCause());
           Errors.message("Error rest /changePassword: "+e.getMessage());
           return new ResponseEntity<>("{\"changed\":false}", HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<>("{\"changed\":false}", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "getUser", method = GET, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<?> getUsuario(@RequestParam("alias") String alias){
       try {
           Usuario usuario = usuariosSession.getUsuario(alias);
           usuario.getUserGrupo().setGuNivelGrupo(null);
           return new ResponseEntity<>(usuario, HttpStatus.ACCEPTED);
       } catch (Exception e) {
           String jsonError = "{\"error\":\""+e.getMessage()+"\"}";
           Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, e);
           Errors.message("Error rest /getUser: "+e.getMessage());
           return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "getUserGroup", method = {GET}, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<?> getUserGroups(){
       GrupoUsuarioSession guSession = new GrupoUsuarioSession(GrupoUsuario.class);
        try {
            List<GrupoUsuario> grupos = guSession.buscarTodos("from GrupoUsuario gu", null);
            for(int i=0; i<grupos.size(); i++){
                grupos.get(i).setGuNivelGrupo(null);
            }
                    
            return new ResponseEntity<>(grupos, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            String jsonError = "{\"error\":\""+ex.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /getUserGroup: "+ex.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
            
        }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "restablecer", method = GET, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> restablecerPassword(@RequestParam("key") String key){
       try {
            Usuario user = new Usuario();
            user = usuariosSession.getUsuarioByToken(key);
            StringBuilder json = new StringBuilder();
            json.append("{\"userAlias\": \"").append(user.getUserAlias()).append("\"}");
            
            return new ResponseEntity<>(json.toString(), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /restablecer|GET: "+ex.getMessage());
            String jsonError = "{\"error\":\""+ex.getMessage()+"\"}";
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
            
        }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "restablecer", method = POST, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> actualizarPassword(@RequestBody ChangeUserCredential credencial){
       try {
            Usuario user = new Usuario();
            user = usuariosSession.getUsuario(credencial.getUserAlias());
            user.setUserPassword(user.hash(credencial.getUserNewpassword(), "SHA"));
            user.setUserToken(user.hash(user.getUserAlias()+user.getUserPassword(), "SHA"));
            usuariosSession.actualizar(user);
            return new ResponseEntity<>("{\"changed\":"+true+"}", HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /restablecer|POST: "+ex.getMessage());
            return new ResponseEntity<>("{\"changed\":"+false+"}", HttpStatus.ACCEPTED);
        }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "reload-properties", method = POST, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> restablecerPassword(){
       try {
            Configuration.restart();
            String json = "{\"status\":\"properties updated\",\"values\":\""+Variables.allVariablesToString()+"\"}";
            return new ResponseEntity<>(json, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            String jsonError = "{\"error\":\""+ex.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /reload-properties: "+ex.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
            
        }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "healthcheck", produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> healthcheck(){
       try {
            String json = "OK";
            return new ResponseEntity<>(json, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            String jsonError = "{\"error\":\""+ex.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /reload-properties: "+ex.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
            
        }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "modulos", method = GET, produces = {MediaType.APPLICATION_JSON})   
   public ResponseEntity<?> getModulosNiveles(){
       try {
           List<ModuloSistema> modulos = msSession.getNivelesAccesos();
           return new ResponseEntity<>(modulos, HttpStatus.ACCEPTED);
       } catch (Exception e) {
           String jsonError = "{\"error\":\""+e.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, e);
            Errors.message("Error rest /modulos: "+e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
       }
   }
   
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "custonLevels", method = GET, produces = {MediaType.APPLICATION_JSON})   
   public ResponseEntity<?> getCustonLevers(@RequestParam("userId") Long id){
       try {
           List<NivelAccesoUsuario> customLevels = nauSession.buscarTodos("from NivelAccesoUsuario nau where nau.nauUser.userId = "+id, null);
           for(int i=0; i<customLevels.size(); i++){
               customLevels.get(i).setLevelActive(Boolean.TRUE);
               customLevels.get(i).getNauModulo().setMsNivelacceso(null);
               customLevels.get(i).getNauUser().setUserGrupo(null);
               customLevels.get(i).getNauModulo().setGrupoUsuario(null);
               customLevels.get(i).getNauUser().setUserActivo(null);
               customLevels.get(i).getNauUser().setUserAlias(null);
               customLevels.get(i).getNauUser().setUserAvatar(null);
               customLevels.get(i).getNauUser().setUserCargo(null);
               customLevels.get(i).getNauUser().setUserCustonAccess(null);
               customLevels.get(i).getNauUser().setUserCustonAccessLevels(null);
               customLevels.get(i).getNauUser().setUserEmail(null);
               customLevels.get(i).getNauUser().setUserGrupo(null);
               customLevels.get(i).getNauUser().setUserNombre(null);
               customLevels.get(i).getNauUser().setUserOnline(null);
               customLevels.get(i).getNauUser().setUserPassword(null);
               customLevels.get(i).getNauUser().setUserToken(null);
           }
           return new ResponseEntity<>(customLevels, HttpStatus.ACCEPTED);
       } catch (Exception e) {
           String jsonError = "{\"error\":\""+e.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, e);
            Errors.message("Error rest /modulos: "+e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
       }
   }
   @CrossOrigin(origins = {"*"})
   @RequestMapping(value = "logout", method = GET, produces = {MediaType.APPLICATION_JSON})
   public ResponseEntity<String> logout(@RequestParam("token") String token){
       try {
            usuariosSession.logout(token);
            return new ResponseEntity<>("", HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            String jsonError = "{\"error\":\""+ex.getMessage()+"\"}";
            Logger.getLogger(UsersRestController.class.getName()).log(Level.SEVERE, null, ex);
            Errors.message("Error rest /logout: "+ex.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.ACCEPTED);
            
        }
   }
   
}
