package jb.CouponsBack.services;

import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.repos.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Scope("singleton")
public class CouponExpirationThread implements Runnable{

    @Autowired
    private CouponRepository couponRepository;

    boolean quit;



        @Override
        public void run() {
           List<Coupon> coupons=new ArrayList<>();

                coupons=couponRepository.findAll();

                for (Coupon coupon: coupons) {
                    if(coupon.getEndDate().isBefore(LocalDateTime.now())){
                        couponRepository.delete(coupon);
                        System.out.println("Coupon with id: "+coupon.getId()+" has expired and is being deleted");
                    }
                }

                try {
                    Thread.sleep(1000*60*60*24);
                } catch (InterruptedException e) {
                }

            }



        public void stop(boolean quit) throws InterruptedException {
            if(!quit) return;
            wait();

        }
    }

