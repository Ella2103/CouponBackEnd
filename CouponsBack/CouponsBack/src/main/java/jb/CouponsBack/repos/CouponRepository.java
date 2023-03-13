package jb.CouponsBack.repos;

import jb.CouponsBack.beans.Category;
import jb.CouponsBack.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    List<Coupon> findByCompanyId(int companyId);

    List<Coupon> findByCompanyIdAndCategory(int companyId, Category category);

    List<Coupon> findByCompanyIdAndPriceLessThan(int companyId, double maxPrice);




}
