package jb.CouponsBack.beans;

import jb.CouponsBack.services.ClientService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class OurSession {
    private ClientService clientService;
    private long lastActive;

    public OurSession() {
    }

    public OurSession(ClientService clientService, long lastActive) {
        this.clientService = clientService;
        this.lastActive = lastActive;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public long getLastActive() {
        return lastActive;
    }
}
