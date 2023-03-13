package jb.CouponsBack.controllers;

import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.repos.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @Autowired
   private CouponRepository couponRepository;

    @GetMapping
    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }
}
