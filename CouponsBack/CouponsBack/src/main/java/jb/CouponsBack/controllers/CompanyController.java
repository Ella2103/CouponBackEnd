package jb.CouponsBack.controllers;

import jb.CouponsBack.beans.Category;
import jb.CouponsBack.beans.Company;
import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.beans.UserDetails;
import jb.CouponsBack.exceptions.AlredyExistsException;
import jb.CouponsBack.exceptions.InvalidException;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.exceptions.NotFoundException;
import jb.CouponsBack.security.TokenManager;
import jb.CouponsBack.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController extends ClientController{

    private CompanyService companyService;
    @Autowired
    private TokenManager tokenManager;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
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

        this.companyService = (CompanyService) tokenManager.getService(token);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/addCoupon")
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon){
        Coupon coup;
        try {
            tokenManager.isSessionInProgress(companyService);
           coup= companyService.addCoupon(coupon);
        } catch (NotFoundException|AlredyExistsException|InvalidException|LoginException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(coup);
    }

    @PutMapping("/updateCoup")
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon){
        Coupon coup;
        try {
            tokenManager.isSessionInProgress(companyService);
            coup=companyService.updateCoupon(coupon);
        } catch (LoginException|InvalidException |NotFoundException|AlredyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(coup);
    }

    @DeleteMapping("/deleteCoup")
    public ResponseEntity<?> deleteCoupon(@RequestBody Coupon coupon){
        Coupon coup;
        try {
            tokenManager.isSessionInProgress(companyService);
            coup=companyService.updateCoupon(coupon);
        } catch (LoginException|InvalidException |NotFoundException|AlredyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok("Coupon "+coup.getTitle()+" was deleted");
    }

    @GetMapping("/coupons")
    public ResponseEntity<?> getCompanyCoupons(){
        try {
            tokenManager.isSessionInProgress(companyService);
        } catch (LoginException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(companyService.getCompanyCoupons());
    }

    @GetMapping("/couponsByCategory")
    public ResponseEntity<?> getCompanyCouponsByCategory(@RequestBody Category category){
        List<Coupon> coupons;
        try {
            tokenManager.isSessionInProgress(companyService);
           coupons= companyService.getCompanyCouponsByCategory(category);
        } catch (LoginException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/couponsByPrice/{maxPrice}")
    public ResponseEntity<?> getCompanyCouponsByMaxPrice(@PathVariable double maxPrice){
        List<Coupon> coupons;
        try {
            tokenManager.isSessionInProgress(companyService);
           coupons= companyService.getCompanyCouponsByMaxPrice(maxPrice);
        } catch (LoginException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/compDets")
    public ResponseEntity<?> getCompanyDetails(){
        Company comp;
        try {
            tokenManager.isSessionInProgress(companyService);
          comp = companyService.getCompanyDetails();
        } catch (LoginException  | InvalidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(comp);
    }

    @GetMapping("/oneCoupon")
    public ResponseEntity<?> getCoupon(@RequestBody Coupon coup){
        Coupon coupon;
        try {
            tokenManager.isSessionInProgress(companyService);
          coupon = companyService.getCoupon(coup.getId());
        } catch (LoginException  | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        tokenManager.updateLastActiveTime(companyService);
        return ResponseEntity.ok(coupon);
    }










    

}
