package at.fhv.teamd.musicshop.customerdbserver.password;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class GeneratePassword {

    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASS;
    private static final String DB_CUSTOMERS_TABLE;

    static {
        DB_URL = "jdbc:postgresql://localhost:5432/postgres";
        DB_USER = "postgres";
        DB_PASS = "f-x_NzzB4E";
        DB_CUSTOMERS_TABLE = "customers";
    }

    public static void main(String[] args) {
        //Defailt password is "123456"
        String password = "123456";

        Set<CustomerDTO> customers = getCustomers();

        assert customers != null;
        customers.forEach(customer -> {
            //Generate salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[32];
            random.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);
            SecretKeyFactory factory;
            byte[] hash;

            try {
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
                hash = factory.generateSecret(spec).getEncoded();

                setPassword(customer, hash, salt);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private static Set<CustomerDTO> getCustomers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + DB_CUSTOMERS_TABLE + " WHERE password is null AND salt is null;")) {

            ResultSet rs = stmt.executeQuery();

            Set<CustomerDTO> customerDTOs = new LinkedHashSet<>();

            while (rs.next()) {
                customerDTOs.add(buildCustomerDTOByResultSet(rs));
            }

            return customerDTOs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CustomerDTO buildCustomerDTOByResultSet(ResultSet rs) throws SQLException {
        return CustomerDTO.builder()
                .withCustomerData(
                        rs.getInt("id"),
                        rs.getString("givenName"),
                        rs.getString("familyName"),
                        rs.getString("birthName"),
                        rs.getString("gender"),
                        rs.getString("birthDate"),
                        rs.getInt("height"),
                        rs.getString("eyecolor"),
                        rs.getString("email"),
                        rs.getString("taxId"),
                        rs.getString("address.addressCountry"),
                        rs.getString("address.addressLocality"),
                        rs.getString("address.postalCode"),
                        rs.getString("address.streetAddress"),
                        rs.getString("address.houseNumber"),
                        rs.getString("phoneNo"),
                        rs.getString("mobileNo"),
                        rs.getString("bankAccount.bank.city"),
                        rs.getString("bankAccount.bank.bankCode"),
                        rs.getString("bankAccount.bank.desc"),
                        rs.getString("bankAccount.bank.bic"),
                        rs.getString("bankAccount.iban"),
                        rs.getString("creditCard.number"),
                        rs.getString("creditCard.type"),
                        rs.getString("creditCard.cvc")
                )
                .build();
    }

    private static void setPassword(CustomerDTO customerDTO, byte[] password, byte[] salt) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + DB_CUSTOMERS_TABLE + " SET password = ?, salt = ? WHERE id = ?")) {

            stmt.setBytes(1, password);
            stmt.setBytes(2, salt);
            stmt.setInt(3, customerDTO.customerId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
