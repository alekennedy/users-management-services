package com.alekennedy.usersmanagement.entities;

/**
 *
 * @author ciac-alejandro
 */
public class ChangeUserCredential {
    private String userAlias;
    private String userOldpassword;
    private String userNewpassword;
    private boolean successful;

    public ChangeUserCredential() {
        //INICIALIZAR EN NULO LOS ATRIBUTOS
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAias) {
        this.userAlias = userAias;
    }

    public String getUserOldpassword() {
        return userOldpassword;
    }

    public void setUserOldpassword(String userOldpassword) {
        this.userOldpassword = userOldpassword;
    }

    public String getUserNewpassword() {
        return userNewpassword;
    }

    public void setUserNewpassword(String userNewpassword) {
        this.userNewpassword = userNewpassword;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
    
}
