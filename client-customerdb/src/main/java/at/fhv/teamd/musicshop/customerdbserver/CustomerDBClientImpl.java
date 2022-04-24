package at.fhv.teamd.musicshop.customerdbserver;

import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class CustomerDBClientImpl extends UnicastRemoteObject implements CustomerDBClient {
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASS;
    private static final String DB_CUSTOMERS_TABLE;
    private static final String DB_DRIVER_FULLY_QUALIFIED_CLASSNAME;
    private static final int LIST_MAX_RESULTS;

    static {
        DB_URL = Objects.requireNonNull(System.getProperty("DB_URL"), "DB_URL system property must be set");
        DB_USER = Objects.requireNonNull(System.getProperty("DB_USER"), "DB_USER system property must be set");
        DB_PASS = Objects.requireNonNull(System.getProperty("DB_PASS"), "DB_PASS system property must be set");
        DB_CUSTOMERS_TABLE = Objects.requireNonNull(System.getProperty("DB_CUSTOMERS_TABLE"), "DB_CUSTOMERS_TABLE system property must be set");
        DB_DRIVER_FULLY_QUALIFIED_CLASSNAME = Objects.requireNonNull(System.getProperty("DB_DRIVER_FULLY_QUALIFIED_CLASSNAME"), "DB_DRIVER_FULLY_QUALIFIED_CLASSNAME system property must be set");
        LIST_MAX_RESULTS = Integer.parseInt(Objects.requireNonNull(System.getProperty("LIST_MAX_RESULTS"), "LIST_MAX_RESULTS system property must be set"));

        try {
            Class.forName(DB_DRIVER_FULLY_QUALIFIED_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void init() {
    }

    public CustomerDBClientImpl() throws RemoteException {
        super(CustomerDBServer.BIND_PORT);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + DB_CUSTOMERS_TABLE + " WHERE \"givenName\" ILIKE ? OR \"familyName\" ILIKE ? OR \"birthName\" ILIKE ? LIMIT " + LIST_MAX_RESULTS + ";")) {

            String searchStr = "%" + name + "%";
            stmt.setString(1, searchStr);
            stmt.setString(2, searchStr);
            stmt.setString(3, searchStr);
            ResultSet rs = stmt.executeQuery();

            Set<CustomerDTO> customerDTOs = new LinkedHashSet<>();

            while (rs.next()) {
                customerDTOs.add(buildCustomerDTOByResultSet(rs));
            }

            return customerDTOs;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomerDBClientException(e.getMessage());
        }
    }

    @Override
    public CustomerDTO findCustomerById(int customerId) throws CustomerDBClientException, CustomerNotFoundException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + DB_CUSTOMERS_TABLE + " WHERE id=?")) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new CustomerNotFoundException();
            }

            return buildCustomerDTOByResultSet(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomerDBClientException(e.getMessage());
        }
    }

    private CustomerDTO buildCustomerDTOByResultSet(ResultSet rs) throws SQLException {
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
}
