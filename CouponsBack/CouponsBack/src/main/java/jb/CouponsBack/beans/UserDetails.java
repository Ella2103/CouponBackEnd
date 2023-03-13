package jb.CouponsBack.beans;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jb.CouponsBack.services.ClientType;

public class UserDetails {

    private ClientType clientType;
    private String email;
    private String password;

    public UserDetails(ClientType clientType, String email, String password) {
        this.clientType = clientType;
        this.email = email;
        this.password = password;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
