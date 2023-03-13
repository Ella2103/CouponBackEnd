package jb.CouponsBack.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "coupons")
@NoArgsConstructor
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne()
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;
    private int amount;
    @Enumerated
    private Category category;
    private String title, description;
    @Lob
    private String image;
    private LocalDateTime startDate, endDate;
    private double price;
    @ManyToMany(fetch = FetchType.EAGER  ,mappedBy = "coupons",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JsonIgnore
    private Set<Customer> customers;


    public Coupon(Company company, int amount, Category category, String title,
                  String description, String image, LocalDateTime startDate, LocalDateTime endDate, double price) {
        this.company = company;
        this.amount = amount;
        this.category = category;
        this.title = title;
        this.description = description;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    //for updating purpose
    public Coupon(int id, Company company, int amount, Category category, String title,
                  String description, String image, LocalDateTime startDate, LocalDateTime endDate, double price) {
        this.id = id;
        this.company = company;
        this.amount = amount;
        this.category = category;
        this.title = title;
        this.description = description;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id == coupon.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyId=" + company.getId() +
                ", amount=" + amount +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                '}'+'\n';
    }
}//end of class
