package jb.CouponsBack.controllers;

import jb.CouponsBack.beans.Company;
import jb.CouponsBack.beans.Customer;
import jb.CouponsBack.beans.UserDetails;
import jb.CouponsBack.exceptions.AlredyExistsException;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.exceptions.NotFoundException;
import jb.CouponsBack.security.TokenManager;
import jb.CouponsBack.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController extends ClientController {

    private AdminService adminService;

    @Autowired
    private TokenManager tokenManager;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }



    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDetails userDetails) {

        String token;
        try {
            token = tokenManager.createToken(userDetails);
        } catch (LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        this.adminService = (AdminService) tokenManager.getService(token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/addCompany")
    public ResponseEntity<?> addCompany(@RequestBody Company company){
        Company comp;
        try {
            tokenManager.isSessionInProgress(adminService);
           comp= adminService.addCompany(company);

        } catch (AlredyExistsException |LoginException e) {
           return ResponseEntity.badRequest().body(e.getMessage());

        }
        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok().body(comp);
    }

    @PutMapping("/updateCompany")
    public ResponseEntity<?> updateCompany(@RequestBody Company company){
        Company comp;
        try {
            tokenManager.isSessionInProgress(adminService);
            comp= adminService.updateCompany(company);
        } catch (NotFoundException | LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok().body(comp);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
       Customer cust;
        try {
            tokenManager.isSessionInProgress(adminService);
            cust=adminService.addCustomer(customer);

        } catch (AlredyExistsException | LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok().body(cust);
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
       Customer cust;
        try {
            tokenManager.isSessionInProgress(adminService);
           cust= adminService.updateCustomer(customer);
        } catch (NotFoundException | LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok().body(cust);
    }

    @DeleteMapping("/delete/company")
    public ResponseEntity<?> deleteCompany(@RequestBody Company company){
        try {
        tokenManager.isSessionInProgress(adminService);

            adminService.deleteCompany(company.getId());
        } catch (NotFoundException | LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);

        return ResponseEntity.ok().body("Company "+ company.getName()+" was deleted successfully");
    }


    @DeleteMapping("/delete/customer")
    public ResponseEntity<?> deleteCustomer(@RequestBody Customer customer){
        try {
        tokenManager.isSessionInProgress(adminService);
            adminService.deleteCustomer(customer.getId());
        } catch (NotFoundException | LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);

        return ResponseEntity.ok().body("Customer "+ customer.getFirstName()+" "+customer.getLastName()+" was deleted successfully");
    }

    @GetMapping("/companies")


    public ResponseEntity<?> getAllCompanies(){
        try {
            tokenManager.isSessionInProgress(adminService);
        } catch (LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);

        return ResponseEntity.ok().body(adminService.getAllCompanies());
    }

    @GetMapping("/companies")
    public ResponseEntity<?> getOneCompany(@RequestBody Company company){
        try {
            tokenManager.isSessionInProgress(adminService);
            adminService.getOneCompany(company.getId());
        } catch (LoginException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok(company);

    }

    @GetMapping("/customers")
    public ResponseEntity<?>getAllCustomers(){
        try {
            tokenManager.isSessionInProgress(adminService);
            adminService.getAllCustomers();
        } catch (LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok(adminService.getAllCustomers());

    }

    @GetMapping("/customers")
    public ResponseEntity<?> getOneCustomer(@RequestBody Customer customer){
        Customer c;
        try {
            tokenManager.isSessionInProgress(adminService);
            c= adminService.getOneCustomer(customer.getId());
        } catch (LoginException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(adminService);
        return ResponseEntity.ok(c);

    }







}
