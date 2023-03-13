package jb.CouponsBack.repos;

import jb.CouponsBack.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {
    boolean existsByName(String name);
    boolean existsByEmailAndPassword(String email,String password);
    boolean existsByEmail(String email);
    Company findByEmail(String email);


}
