package com.alekennedy.usersmanagement.mail;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author alejandro
 */
public class EmailWrapper {
    private String senderEmail;
    private String senderPass;
    private String toEmail;
    private String smtpServer;
    private String smtpPort;

    public EmailWrapper() {
        this.senderEmail = "";
        this.senderPass = "";
        this.smtpServer = "smtp.mail.com";
        this.smtpPort = "465";
        this.toEmail = "";
    }

    public EmailWrapper(String senderEmail, String senderPass, String toEmail, String smtpServer, String smtpPort) {
        this.senderEmail = senderEmail;
        this.senderPass = senderPass;
        this.toEmail = toEmail;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
    }
    
    /**
     * Setting smtp server properties
     * @return Properties Object
     */
    private Properties initSettings(){
        Properties props = new Properties();
        
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", this.smtpServer);
        props.put("mail.smtp.socketFactory.port", this.smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp..port", this.smtpPort);        
        
        return props;
    }
    
    /**
     * Open Session with email's user and password  to send message
     * @return Session object
     */
    private Session conexionStablished(){
        Session session;
        session = Session.getInstance(initSettings(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPass);
            }
            
        });
        
        return session;
    }
    
    /**
     * Send message by email
     * @param subject subject of email
     * @param message message to send by email
     * @throws MessagingException 
     */
    public void sendEmail(String subject, String message) throws MessagingException{
        Logger.getLogger(EmailWrapper.class.getCanonicalName()).log(Level.INFO,"Establiciendo Conexion");
        Session session = conexionStablished();
        
        MimeMessage mimeMessage = new MimeMessage(session);        
        InternetAddress[] address = InternetAddress.parse(toEmail, true);
        
        mimeMessage.setRecipients(Message.RecipientType.TO, address);        
        mimeMessage.setSubject(subject);
        mimeMessage.setText(message);
        mimeMessage.setSentDate(new Date());
        mimeMessage.setHeader("XPriority", "1");
        Logger.getLogger(EmailWrapper.class.getCanonicalName()).log(Level.INFO,"Enviando email...");
        Transport.send(mimeMessage);        
        Logger.getLogger(EmailWrapper.class.getCanonicalName()).log(Level.INFO,"Email enviado con exito!");
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPass() {
        return senderPass;
    }

    public void setSenderPass(String senderPass) {
        this.senderPass = senderPass;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }    
}
