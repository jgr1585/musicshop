package at.fhv.teamd.musicshop.backend.domain.person;

import java.time.LocalDate;

public class Customer extends Person {
    private final int customerNo;
    private String givenName;
    private String familyName;
    private String birthName;
    private String gender;
    private LocalDate birthDate;
    private Integer height;
    private String eyecolor;
    private String email;
    private String taxId;
    private Address address;
    private String phoneNo;
    private String mobileNo;
    private Bank bank;
    private CreditCard creditCard;

    public Customer(String firstname, String lastname, int customerNo) {
        super(firstname, lastname);

        this.customerNo = customerNo;
    }
}
