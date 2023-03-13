package jb.CouponsBack.services;

import jb.CouponsBack.exceptions.LoginException;
import jb.CouponsBack.repos.CompanyRepository;
import jb.CouponsBack.repos.CouponRepository;
import jb.CouponsBack.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public abstract class ClientService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CouponRepository couponRepository;

    abstract boolean login(String email, String password) throws LoginException;


}
