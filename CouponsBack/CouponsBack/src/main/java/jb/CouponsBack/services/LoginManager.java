package jb.CouponsBack.services;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jb.CouponsBack.exceptions.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class LoginManager {
    @Autowired
    private final ApplicationContext applicationContext;

    public LoginManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public ClientService login(String email, String password, ClientType type) throws LoginException {
        switch (type) {
            case Admin:
                AdminService adminService = applicationContext.getBean(AdminService.class);
                if(!adminService.login(email,password))
                    throw new LoginException("Wrong email or password");

                return adminService;

            case Customer:
                CustomerService customerFacade = applicationContext.getBean(CustomerService.class);
                if(!customerFacade.login(email, password))
                    throw new LoginException("Wrong email or password");

                return customerFacade;

            case Company:
                CompanyService companyFacade = applicationContext.getBean(CompanyService.class);
                if (!companyFacade.login(email, password))
                    throw new LoginException("Wrong email or password");
                return companyFacade;

            default:
                throw new IllegalStateException("Unexpected Client type: " + type);
        }

    }
}