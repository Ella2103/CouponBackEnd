package jb.CouponsBack.repos;

import jb.CouponsBack.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    boolean existsByFirstNameAndLastName(String firstName,String lastName);
    boolean existsByEmailAndPassword(String email, String password);
    Customer findByEmail(String email);
    @Query(value = "SELECT coupon_id FROM customers_vs_coupons WHERE customer_id=:customerId",nativeQuery = true)
    List<Integer> findCouponsByCustomerId(int customerId);
    @Query(value = "SELECT customer_id FROM customers_vs_coupons WHERE coupon_id=:couponId",nativeQuery = true)
    List<Integer> findCustomersByCouponId(int couponId);



}
