package at.fhv.teamd.musicshop.library.dto;

import java.io.Serializable;
import java.util.Objects;

public final class CustomerDTO implements Serializable {
    private static final long serialVersionUID = -6243467419374046090L;

    private Integer customerId;
    private String givenName;
    private String familyName;
    private String birthName;
    private String gender;
    private String birthDate;
    private Integer height;
    private String eyecolor;
    private String email;
    private String taxId;
    private String addressCountry;
    private String addressLocality;
    private String addressPostalCode;
    private String addressStreet;
    private String addressHouseNumber;
    private String phoneNo;
    private String mobileNo;
    private String bankAccountCity;
    private String bankAccountCode;
    private String bankAccountDesc;
    private String bankAccountBic;
    private String bankAccountIban;
    private String creditCardNumber;
    private String creditCardType;
    private String creditCardCvc;

    public static CustomerDTO.Builder builder() {
        return new CustomerDTO.Builder();
    }

    public Integer customerId() {
        return this.customerId;
    }

    public String givenName() {
        return this.givenName;
    }

    public String familyName() {
        return this.familyName;
    }

    public String birthName() {
        return this.birthName;
    }

    public String gender() {
        return this.gender;
    }

    public String birthDate() {
        return this.birthDate;
    }

    public Integer height() {
        return this.height;
    }

    public String eyecolor() {
        return this.eyecolor;
    }

    public String email() {
        return this.email;
    }

    public String taxId() {
        return this.taxId;
    }

    public String addressCountry() {
        return this.addressCountry;
    }

    public String addressLocality() {
        return this.addressLocality;
    }

    public String addressPostalCode() {
        return this.addressPostalCode;
    }

    public String addressStreet() {
        return this.addressStreet;
    }

    public String addressHouseNumber() {
        return this.addressHouseNumber;
    }

    public String phoneNo() {
        return this.phoneNo;
    }

    public String mobileNo() {
        return this.mobileNo;
    }

    public String bankAccountCity() {
        return this.bankAccountCity;
    }

    public String bankAccountCode() {
        return this.bankAccountCode;
    }

    public String bankAccountDesc() {
        return this.bankAccountDesc;
    }

    public String bankAccountBic() {
        return this.bankAccountBic;
    }

    public String bankAccountIban() {
        return this.bankAccountIban;
    }

    public String creditCardNumber() {
        return this.creditCardNumber;
    }

    public String creditCardType() {
        return this.creditCardType;
    }

    public String creditCardCvc() {
        return this.creditCardCvc;
    }

    private CustomerDTO() {
    }

    public static class Builder {
        private final CustomerDTO instance;

        private Builder() {
            this.instance = new CustomerDTO();
        }

        public Builder withCustomerData(
            Integer customerId,
            String givenName,
            String familyName,
            String birthName,
            String gender,
            String birthDate,
            Integer height,
            String eyecolor,
            String email,
            String taxId,
            String addressCountry,
            String addressLocality,
            String addressPostalCode,
            String addressStreet,
            String addressHouseNumber,
            String phoneNo,
            String mobileNo,
            String bankAccountCity,
            String bankAccountCode,
            String bankAccountDesc,
            String bankAccountBic,
            String bankAccountIban,
            String creditCardNumber,
            String creditCardType,
            String creditCardCvc
        ) {
            this.instance.customerId = customerId;
            this.instance.givenName = givenName;
            this.instance.familyName = familyName;
            this.instance.birthName = birthName;
            this.instance.gender = gender;
            this.instance.birthDate = birthDate;
            this.instance.height = height;
            this.instance.eyecolor = eyecolor;
            this.instance.email = email;
            this.instance.taxId = taxId;
            this.instance.addressCountry = addressCountry;
            this.instance.addressLocality = addressLocality;
            this.instance.addressPostalCode = addressPostalCode;
            this.instance.addressStreet = addressStreet;
            this.instance.addressHouseNumber = addressHouseNumber;
            this.instance.phoneNo = phoneNo;
            this.instance.mobileNo = mobileNo;
            this.instance.bankAccountCity = bankAccountCity;
            this.instance.bankAccountCode = bankAccountCode;
            this.instance.bankAccountDesc = bankAccountDesc;
            this.instance.bankAccountBic = bankAccountBic;
            this.instance.bankAccountIban = bankAccountIban;
            this.instance.creditCardNumber = creditCardNumber;
            this.instance.creditCardType = creditCardType;
            this.instance.creditCardCvc = creditCardCvc;
            return this;
        }

        public CustomerDTO build() {
            Objects.requireNonNull(this.instance.customerId, "customerId must be set in CustomerDTO");
            return this.instance;
        }
    }
}
