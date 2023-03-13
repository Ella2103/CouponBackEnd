package jb.CouponsBack.controllers;

import jb.CouponsBack.beans.Category;
import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.beans.Customer;
import jb.CouponsBack.beans.UserDetails;
import jb.CouponsBack.exceptions.AlredyExistsException;
import jb.CouponsBack.exceptions.ExpiredCouponException;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.exceptions.NotFoundException;
import jb.CouponsBack.security.TokenManager;
import jb.CouponsBack.services.CompanyService;
import jb.CouponsBack.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController extends ClientController {

    private CustomerService customerService;
    @Autowired
    private TokenManager tokenManager;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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

        this.customerService = (CustomerService) tokenManager.getService(token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon) {

        try {
            tokenManager.isSessionInProgress(customerService);
            customerService.purchaseCoupon(coupon);
        } catch (LoginException | AlredyExistsException | ExpiredCouponException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok("Coupon was purchaesd");
    }

    @GetMapping("/coupons")
    public ResponseEntity<?> getCustomerCoupons() {
        List<Coupon> coupons;
        try {
            tokenManager.isSessionInProgress(customerService);
            coupons=customerService.getCustomerCoupons();
        } catch (LoginException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok(coupons);
    }
    @GetMapping("/couponsByCategory")
    public ResponseEntity<?> getCustomerCouponsByCategory(@RequestBody Category category){
        List<Coupon> coupons;
        try {
            tokenManager.isSessionInProgress(customerService);
            coupons=customerService.getCustomerCouponsByCategory(category);
        } catch (LoginException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/couponsByPrice/{maxPrice}")
    public ResponseEntity<?> getCustomerCouponsByPrice(@PathVariable double maxPrice){
        List<Coupon> coupons;
        try {
            tokenManager.isSessionInProgress(customerService);
            coupons=customerService.getCustomerCouponsByPrice(maxPrice);
        } catch (LoginException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok(coupons);
    }
    @GetMapping
    public ResponseEntity<?> getAllCoupons(){
        List<Coupon> coupons;
        try {
            tokenManager.isSessionInProgress(customerService);
            coupons=customerService.getAllCoupons();
        } catch (LoginException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/customerDets")
    public ResponseEntity<?> getCustomerDetails(){
        Customer customer;
        try {
            tokenManager.isSessionInProgress(customerService);
            customer=customerService.getCustomerDetails();
        } catch (LoginException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/deletePurchase")
    public ResponseEntity<?> deleteCouponPurchase(@RequestBody Coupon coupon){

        try {
            tokenManager.isSessionInProgress(customerService);
            customerService.deleteCouponPurchase(coupon.getId());
        } catch (LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(customerService);

        return ResponseEntity.ok("Coupon "+coupon.getTitle()+" was deleted");
    }







}
