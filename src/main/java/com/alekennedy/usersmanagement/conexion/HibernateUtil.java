package com.alekennedy.usersmanagement.conexion;

import com.alekennedy.usersmanagement.entities.GrupoUsuario;
import com.alekennedy.usersmanagement.entities.ModuloSistema;
import com.alekennedy.usersmanagement.entities.NivelAcceso;
import com.alekennedy.usersmanagement.entities.NivelAccesoUsuario;
import com.alekennedy.usersmanagement.entities.Usuario;
import com.alekennedy.usersmanagement.etc.SystemVariables;
//import com.mycompany.propertiesmanagement.etc.SystemVariables;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
/**
 *
 * @author Alejandro
 * 
 * Clase que establece metodos de conexion a la base de datos por medio de 
 * hibernate
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory = null;       
    private static boolean iniciado = false;

    private HibernateUtil() {
        //constructor por default
    }
    
    
    
   public static void establecerConexion()throws HibernateException{
        try 
        { 
            Configuration cfg = new Configuration();
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
            cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://"+SystemVariables.getDataBaseIp()+":"+SystemVariables.getDataBasePort()+"/"+SystemVariables.getDataBaseName());
            cfg.setProperty("hibernate.connection.username", SystemVariables.getDataBaseUser());
            cfg.setProperty("hibernate.connection.password", SystemVariables.getDataBasePassword());
            cfg.setProperty("hibernate.show_sql", "true");
            cfg.setProperty("hibernate.use_sql_comments", "true");
            cfg.setProperty("hibernate.format_sql", "true");
            cfg.setProperty("hibernate.connection.autocommit","false");
            
            
            //Setrting pool conexion
            //cfg.setProperty("connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
            cfg.setProperty(Environment.C3P0_MIN_SIZE, "5");
            cfg.setProperty(Environment.C3P0_MAX_SIZE, "20");
            cfg.setProperty(Environment.C3P0_ACQUIRE_INCREMENT, "5");
            cfg.setProperty(Environment.C3P0_TIMEOUT,"100");
            cfg.setProperty(Environment.C3P0_MAX_STATEMENTS, "1000");
            cfg.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
            
            cfg.setProperty("hibernate.hbm2ddl.auto", "update");
            //cfg.setProperty("hibernate.hbm2ddl.auto", "none");
            cfg.addAnnotatedClass(Usuario.class);
            cfg.addAnnotatedClass(GrupoUsuario.class);
            cfg.addAnnotatedClass(NivelAcceso.class);
            cfg.addAnnotatedClass(ModuloSistema.class);
            cfg.addAnnotatedClass(NivelAccesoUsuario.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
                               
            
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
            
            
            iniciado = true;
        } catch (HibernateException he) 
        {            
           iniciado = false;           
           if(sessionFactory!=null){               
               closeSessionFactory();
           }           
           throw new HibernateException(he);
        }
    }

    public static SessionFactory getSessionFactory() 
    {         
        return sessionFactory; 
    } 
    
    public static  boolean isInit(){
        return iniciado;
    }
    
    public static void closeSessionFactory(){    
        if(sessionFactory!=null && !sessionFactory.isClosed()){
            
            sessionFactory.close();            
                        
        }        
        iniciado = false;        
    }
        
}
