package jb.CouponsBack.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jb.CouponsBack.beans.OurSession;
import jb.CouponsBack.beans.UserDetails;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service

public class TokenManager {
    @Autowired
    private LoginManager loginManager;
    @Autowired
    private  HashMap<String , OurSession> sessions;
    @Autowired
    HttpServletResponse response;


    private static final int TOKEN_VALIDITY = 30; // minutes


    public TokenManager() {

    }

    public String createToken(UserDetails userDetails) throws LoginException {

        ClientService service = loginManager.login(userDetails.getEmail(), userDetails.getPassword(), userDetails.getClientType());




        String secret = "NikkiMiloSecret";
        String issuer = "EllaGreyton";
        if(service instanceof CustomerService){
            String token=JWT.create().withIssuer(issuer)
                    .withClaim("id",((CustomerService) service).getCustomerId())
                    .withClaim("email",userDetails.getEmail())
                    .withClaim("clientType",userDetails.getClientType().toString())
                    .withIssuedAt(new Date(System.currentTimeMillis()))

                    .sign(Algorithm.HMAC256(secret));
            response.setHeader("Authorization",token);
            sessions.put(token,new OurSession(service, System.currentTimeMillis()));
            return token;

        }
        if(service instanceof CompanyService){
            String token=JWT.create().withIssuer(issuer)
                    .withClaim("id",((CompanyService) service).getCompanyId())
                    .withClaim("email",userDetails.getEmail())
                    .withClaim("clientType",userDetails.getClientType().toString())
                    .withIssuedAt(new Date(System.currentTimeMillis()))

                    .sign(Algorithm.HMAC256(secret));
            response.setHeader("Authorization",token);

            sessions.put(token,new OurSession(service, System.currentTimeMillis()));
            return token;

        } else if (service instanceof AdminService) {
            String token=JWT.create().withIssuer(issuer)
                    .withClaim("id",0)
                    .withClaim("email",userDetails.getEmail())
                    .withClaim("clientType",userDetails.getClientType().toString())
                    .withIssuedAt(new Date(System.currentTimeMillis()))

                    .sign(Algorithm.HMAC256(secret));
            response.setHeader("Authorization",token);
            sessions.put(token,new OurSession(service, System.currentTimeMillis()));
            return token;
        }
        throw new LoginException("invalid user");
        }


        public OurSession findSessionByToken(String token){
            return sessions.getOrDefault(token, null);
        }



        public void updateLastActiveTime(String token){
        findSessionByToken(token).setLastActive(System.currentTimeMillis());
        }

    public void updateLastActiveTime(ClientService clientService){
        for (HashMap.Entry<String, OurSession> entry : sessions.entrySet()) {
            OurSession session = entry.getValue();
            if (session.getClientService() == clientService) {
                session.setLastActive(System.currentTimeMillis());
                break;
            }
        }
    }
    public void isSessionInProgress(ClientService clientService) throws LoginException {
        boolean b = sessions.values().stream().anyMatch(s -> s.getClientService() == clientService);
        if(!b) throw new LoginException("Your session has timed out, please login again");
    }



        @Scheduled(fixedRate = 1000*60)
    public void removeExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> (currentTime - entry.getValue().getLastActive()) >= TimeUnit.MINUTES.toMillis(TOKEN_VALIDITY));
    }

    public ClientService getService(String token){
        OurSession session=sessions.get(token);
        return session.getClientService();

    }

    }
