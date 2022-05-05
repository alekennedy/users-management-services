/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alekennedy.usersmanagement.thread;

import com.alekennedy.usersmanagement.mail.EmailWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author ale
 */
public class EnviarEmail implements Runnable{
    private String emailAsunto;
    private String emailMensaje;
    private String email;

    public EnviarEmail(String emailAsunto, String emailMensaje, String email) {
        this.emailAsunto = emailAsunto;
        this.emailMensaje = emailMensaje;
        this.email = email;
    }

    
    
    
    @Override
    public void run(){
        EmailWrapper emailSender = new EmailWrapper();
        emailSender.setSenderEmail("mailexample@mail.com");
        emailSender.setSenderPass("nucie.pti");
        emailSender.setSmtpServer("smtp.gmail.com");
        emailSender.setSmtpPort("465");
        emailSender.setToEmail(this.email);
        try {
            emailSender.sendEmail(this.emailAsunto, this.emailMensaje);
        } catch (MessagingException ex) {
            Logger.getLogger(EnviarEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
