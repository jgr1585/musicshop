package at.fhv.teamd.musicshop.backend.domain.customer;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
public class Customer {
    @Id
    private String userName;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String birthdate;

    @Column
    private String email;

    @Column
    private String street;

    @Column
    private String zipcode;

    @Column
    private String city;

    @Column
    private String country;

    @Enumerated(EnumType.STRING)
    private CreditCardType creditcardType;

    @Column
    private String creditcardNo;

    @Column
    private String creditcardCVC;

    protected Customer() {}

    public Customer(String userName, String firstname, String lastname, Gender gender, String birthdate, String email, String street, String zipcode, String city, String country, CreditCardType creditcardType, String creditcardNo, String creditcardCVC) {
        this.userName = Objects.requireNonNull(userName);
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
        this.gender = Objects.requireNonNull(gender);
        this.birthdate = Objects.requireNonNull(birthdate);
        this.email = Objects.requireNonNull(email);
        this.street = Objects.requireNonNull(street);
        this.zipcode = Objects.requireNonNull(zipcode);
        this.city = Objects.requireNonNull(city);
        this.country = Objects.requireNonNull(country);
        this.creditcardType = Objects.requireNonNull(creditcardType);
        this.creditcardNo = Objects.requireNonNull(creditcardNo);
        this.creditcardCVC = Objects.requireNonNull(creditcardCVC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return userName.equals((customer.userName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
