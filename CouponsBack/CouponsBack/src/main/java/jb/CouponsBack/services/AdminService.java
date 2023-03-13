package jb.CouponsBack.services;


import jb.CouponsBack.beans.Company;
import jb.CouponsBack.beans.Coupon;
import jb.CouponsBack.beans.Customer;
import jb.CouponsBack.exceptions.AlredyExistsException;
import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.exceptions.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public class AdminService extends ClientService {


    public AdminService() {
    }

    @Override
    boolean login(String email, String password) throws LoginException {
        if(email.equals("admin@admin.com") && password.equals("admin"))
            return true;
       throw  new LoginException("Wrong email or password.");
    }

    public Company addCompany(Company company) throws AlredyExistsException {
        if(companyRepository.existsByName(company.getName())||companyRepository.existsByEmail(company.getEmail())) throw new
                AlredyExistsException("Can't add the same company twice.");
        System.out.println("Company: "+company.getName()+" added successfully.");
        return companyRepository.save(company);


    }


    public Company updateCompany(Company company) throws NotFoundException {
        if(!companyRepository.existsById(company.getId())) throw new NotFoundException("Company wasn't found.");


            Company comp= companyRepository.save(company);
            System.out.println("Company: " + company.getId() + " was updated.");
            return comp;


    }

    public void deleteCompany(int companyId) throws NotFoundException {
        Company company=companyRepository.findById(companyId).orElseThrow(()->new NotFoundException("company with this id wasnt found"));
        if(company.getCoupons().size()<=0){companyRepository.deleteById(companyId);
        return;
        }
        List<Coupon> companyCoupons=couponRepository.findByCompanyId(companyId);
        for (Coupon compCoup:companyCoupons) {
            int coupId = compCoup.getId();
            if (compCoup.getCustomers().size() > 0) {
                List<Integer> customersIds = customerRepository.findCustomersByCouponId(coupId);
                for (Integer customerId : customersIds) {
                    Customer customer = customerRepository.findById(customerId).orElseThrow();
                    customer.getCoupons().remove(compCoup);
                    compCoup.getCustomers().remove(customer);
                    customerRepository.save(customer);
                    couponRepository.deleteById(coupId);
                    System.out.println("coupon deleted from customers");
                }
            } else {

                couponRepository.deleteById(coupId);
            }
        }
        companyRepository.deleteById(companyId);
        System.out.println("company was deleted");


        }




    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public Company getOneCompany(int companyId) throws NotFoundException {
        return companyRepository.findById(companyId).orElseThrow(()->new NotFoundException("Invalid company id"));
    }

    public Customer addCustomer(Customer customer) throws AlredyExistsException {
        if(customerRepository.existsByFirstNameAndLastName(customer.getFirstName(),customer.getLastName())) throw
        new AlredyExistsException("Can not add the same customer twice");
        System.out.println("Customer: "+customer.getFirstName()+" "+customer.getLastName()+" was added.");
        return  customerRepository.save(customer);

    }

    public Customer updateCustomer(Customer customer) throws NotFoundException {
        if(!customerRepository.existsById(customer.getId())) throw new NotFoundException("Customer  wasn't found");
       Customer cust= customerRepository.save(customer);
        System.out.println("Customer: "+customer.getId()+" was updated. ");
        return cust;
    }
    public void deleteCustomer(int customerId) throws NotFoundException {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException("Customer with this id wasn't found"));
        if(customer.getCoupons().size()==0){customerRepository.deleteById(customerId);
        return;}
        else {
            customer.setCoupons(null);
            customerRepository.save(customer);
            customerRepository.deleteById(customerId);
        }
        System.out.println("Customer with ID: "+customerId+" was deleted.");
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getOneCustomer(int customerId) throws NotFoundException {
        return customerRepository.findById(customerId).orElseThrow(()->new NotFoundException("Customer not found"));
    }






    }//end
