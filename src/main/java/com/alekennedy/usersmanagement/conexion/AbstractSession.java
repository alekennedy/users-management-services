package com.alekennedy.usersmanagement.conexion;


import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import py.com.lib.util.log.Messages;

/**
 *
 * @author Alejandro
 * 
 * Clase padre para los metodos de persistencia y consulta de 
 * la base de datos por medio de hibernate
 */

public class AbstractSession<T> {
    private Session session;
    private Transaction tx;  
    private Class<T> entityClass; 

    public AbstractSession(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    
        
    public boolean iniciarOperacion() throws HibernateException{              
        
        try {
            if(!HibernateUtil.isInit()){
                HibernateUtil.establecerConexion();
            }            
            session = HibernateUtil.getSessionFactory().openSession();                  
            tx = session.beginTransaction();
            return  HibernateUtil.isInit();
        } catch (HibernateException e) {            
            HibernateUtil.closeSessionFactory();
            throw new HibernateException(e);            
        }
        
    }
    
        
    public void manejarExcepcion(Exception he)throws HibernateException{
        if(HibernateUtil.isInit()){
            tx.rollback();               
        }        
        throw  new HibernateException("Ocurrio un error en la capa de acceso de datos", he);
    }
    
    public void closeSession(){
        if(session!=null && session.isOpen()){   
            Messages.append("Database conection closed.");
            session.close();
        }else{
            Messages.append("Not close database connection: "+session.isOpen()+" -->"+session.toString());
        }                                  
    }
    
    public Long guardar(T objeto) throws HibernateException{
        Long id = 0l;
        try {
            iniciarOperacion();            
            id = (Long)session.save(objeto);            
            tx.commit();
        } catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
        return id;
    }
    
    public void actualizar(T objeto) throws HibernateException{
        try {
            iniciarOperacion();
            session.update(objeto);
            tx.commit();
        } catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
    }
    
    public void eliminar(T objeto) throws HibernateException{
        try {
            iniciarOperacion();
            session.delete(objeto);
            tx.commit();            
        } catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
    }
    
    public T obtener(Long idObjeto, T o)throws HibernateException{        
        try {
            iniciarOperacion();
            o =  (T) session.get(o.getClass(), idObjeto);
        }finally{
            closeSession();
        }
        return o;
    }
    
    public List<T> buscarTodos(String queryString, Integer max)throws HibernateException{
        try {
            iniciarOperacion();            
            Query query = session.createQuery(queryString);
            
            if(max!=null){
                query.setMaxResults(max);
            }
            return query.list();
        }  catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
    }
    
    public T buscarUnicoResult(String queryString)throws HibernateException{
        try {
            iniciarOperacion();            
            Query query = session.createQuery(queryString);
                        
            return (T) query.uniqueResult();
        }  catch (HibernateException e) {
            manejarExcepcion(e);
            throw  e;
        }
        finally{
            closeSession();
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    
    
       
}
