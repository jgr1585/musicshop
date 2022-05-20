package at.fhv.teamd.musicshop.backend.domain.customer;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
public class Customer {
    @Id
    private int customerNo;

    @Column(unique = true)
    private String userName;

    @Column
    private String creditcardNo;

    protected Customer() {}

    public Customer(int customerNo, String userName, String creditcardNo) {
        this.customerNo = customerNo;
        this.userName = userName;
        this.creditcardNo = creditcardNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerNo == customer.customerNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerNo);
    }
}
