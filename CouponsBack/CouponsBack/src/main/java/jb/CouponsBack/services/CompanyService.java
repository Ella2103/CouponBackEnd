package jb.CouponsBack.services;


import jb.CouponsBack.beans.Category;
import jb.CouponsBack.beans.Company;
import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.beans.Customer;
import jb.CouponsBack.exceptions.AlredyExistsException;
import jb.CouponsBack.exceptions.InvalidException;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.exceptions.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@Scope("prototype")
public class CompanyService extends ClientService {
    private static int companyId;



    @Override
   boolean login(String email, String password) throws LoginException {
        if(companyRepository.existsByEmailAndPassword(email,password)){
            Company c=companyRepository.findByEmail(email);
            companyId=c.getId();
            System.out.println("Welcome "+c.getName());
            return true;
        }
        throw new LoginException("Wrong email or password");
    }


    public Coupon addCoupon(Coupon coupon) throws NotFoundException, AlredyExistsException, InvalidException {
        if(coupon.getCompany().getId()!=companyId)throw new InvalidException("Invalid company ID");
        List<Coupon> coupons=couponRepository.findByCompanyId(companyId);
        if(coupons.stream().anyMatch((c)->c.getTitle().equals(coupon.getTitle()))) throw
                new AlredyExistsException("A coupon with this title already exists.");
        if(coupons.stream().anyMatch((c)->c.getId()==(coupon.getId()))) throw
                new AlredyExistsException("A coupon with this ID already exists.Maybe you want to update?");


        System.out.println("Coupon was added");
       return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Coupon coupon) throws AlredyExistsException, InvalidException, NotFoundException {
        if(coupon.getCompany().getId()!=companyId) throw new InvalidException("Coupon's company ID doesn't match");
        List<Coupon> coupons=couponRepository.findByCompanyId(companyId);
        System.out.println(coupons);
        if(coupons.stream().anyMatch((c)->c.getTitle().equals(coupon.getTitle()))) throw new AlredyExistsException("A coupon with this title already exists.");
        if(coupons.stream().anyMatch((c)->(c.getId()==(coupon.getId())))) {
            System.out.println("Coupon was updated.");
            return couponRepository.save(coupon);
        }else throw new NotFoundException("Coupon with this ID doesn't exist");

    }

    public void deleteCoupon(int couponId) throws NotFoundException, InvalidException {
        Coupon coupon=couponRepository.findById(couponId).orElseThrow(()->new NotFoundException("Coupon with this id wasn't found"));
        if(coupon.getCompany()==null)  throw new InvalidException("Company Id doesn't match");
        if(coupon.getCompany().getId()!=companyId) throw new InvalidException("Company Id doesn't match");
        List<Customer> customers=customerRepository.findAll();
        List<Integer> customersIds= customerRepository.findCustomersByCouponId(couponId);
        for (Customer customer: customers ) {
            int id=customer.getId();
            for (Integer customerId : customersIds) {
                if(id==customerId){
                    customer.getCoupons().remove(coupon);
                    coupon.getCustomers().remove(customer);
                    customerRepository.save(customer);
                    System.out.println("coupon deleted from customers");
                }
            }
        }
       Company company= companyRepository.findById(companyId).orElseThrow();
        company.getCoupons().remove(coupon);
        companyRepository.save(company);
        couponRepository.deleteById(coupon.getId());
           System.out.println("Coupon "+coupon.getTitle()+" was deleted.");

    }


    public List<Coupon> getCompanyCoupons(){
        return couponRepository.findByCompanyId(companyId);
    }


    public List<Coupon> getCompanyCouponsByCategory(Category category) throws NotFoundException {
        if(couponRepository.findByCompanyIdAndCategory(companyId,category).size()==0)
            throw new NotFoundException("No coupons in this category");
        System.out.println("Coupons by category: "+category+"::::>>>>");
        return couponRepository.findByCompanyIdAndCategory(companyId,category);
    }

    public List<Coupon> getCompanyCouponsByMaxPrice(double maxPrice) throws NotFoundException {
        if(couponRepository.findByCompanyIdAndPriceLessThan(companyId,maxPrice).size()==0)
            throw new NotFoundException("No coupons were found");
        System.out.println("Coupons by max price: "+maxPrice+"::::>>>>");
        return couponRepository.findByCompanyIdAndPriceLessThan(companyId,maxPrice);
    }

    public Company getCompanyDetails() throws InvalidException {
        return companyRepository.findById(companyId).orElseThrow(()->new InvalidException("OMG SOMETHING HAPPENED"));
    }

    public Coupon getCoupon(int couponId) throws NotFoundException {
        Coupon coupon=getCompanyCoupons().stream().filter(c-> c.getId()==couponId).findAny().
                orElseThrow(()-> new NotFoundException("coupon with this id wasnt found"));
        return coupon;
    }


    public int getCompanyId() {
        return companyId;
    }
}//end
