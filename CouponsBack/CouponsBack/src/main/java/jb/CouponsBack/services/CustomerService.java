package jb.CouponsBack.services;

import jb.CouponsBack.beans.Category;
import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.beans.Customer;
import jb.CouponsBack.exceptions.AlredyExistsException;
import jb.CouponsBack.exceptions.ExpiredCouponException;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.exceptions.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Service
@Scope("prototype")
public class CustomerService extends ClientService {
    private static int customerId;


    @Override
     boolean login(String email, String password) throws LoginException {
        if(customerRepository.existsByEmailAndPassword(email,password)) {
            Customer c=customerRepository.findByEmail(email);
            customerId=c.getId();
            System.out.println("Welcome "+c.getFirstName()+" "+c.getLastName());
            return true;
        }else   throw new LoginException("Wrong email or password");

    }


    public void purchaseCoupon(Coupon coupon) throws AlredyExistsException, ExpiredCouponException {
        Customer customer=customerRepository.findById(customerId).orElseThrow();
        if(coupon.getEndDate().isBefore(LocalDateTime.now()))throw new ExpiredCouponException();
        List<Coupon> coupons=customer.getCoupons();
        if(coupons.contains(coupon)) throw new AlredyExistsException("You bought this coupon already");
        else {
            coupons.add(coupon);
            customer.setCoupons(coupons);
            customerRepository.save(customer);
            System.out.println("Coupon with ID "+coupon.getId()+" purchased. Thank you "+customer.getFirstName());
        }
    }

    public List<Coupon> getCustomerCoupons()  {
        return customerRepository.findById(customerId).get().getCoupons();
    }

    public List<Coupon> getCustomerCouponsByCategory(Category category)  {
        System.out.println("Coupons by category: "+category+"::::>>>>");
        List<Coupon> coupons=new ArrayList<>();
        getCustomerCoupons().stream().filter((c)->c.getCategory().equals(category)).forEach((c)->coupons.add(c));
        return coupons;
    }

    public List<Coupon> getCustomerCouponsByPrice(double maxPrice)  {
        System.out.println("Coupons by max price: "+maxPrice+"::::>>>>");
        List<Coupon> coupons=new ArrayList<>();
        getCustomerCoupons().stream().filter((c)->c.getPrice()<=maxPrice).forEach((c)->coupons.add(c));
        return coupons;
    }

    public Customer getCustomerDetails() throws NotFoundException {
        return customerRepository.findById(customerId).orElseThrow(()->new NotFoundException("Customer not found"));
    }

    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }

    public void deleteCouponPurchase(int couponId){
        Customer customer=customerRepository.findById(customerId).orElseThrow();
        List<Coupon> coupons =getCustomerCoupons();
        for (Coupon coupon: coupons) {
            if(coupon.getId()==couponId){
                coupons.remove(coupon);
                customer.setCoupons(coupons);
                customerRepository.save(customer);
                System.out.println("coupon with ID"+couponId+"was deleted from your purchases");
                break;
            }

        }
    }

    public int getCustomerId(){
        return customerId;
    }



}//end
