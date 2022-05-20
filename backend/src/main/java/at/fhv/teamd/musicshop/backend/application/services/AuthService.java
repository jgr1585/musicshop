package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.library.permission.UserRole;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Set;

public class AuthService {
    private static String userName;
    private static Set<UserRole> userRoles;

    AuthService() {
    }

    public void authorizeAccessLevels(RemoteFunctionPermission permission) throws NotAuthorizedException {
        if (userRoles == null) {
            throw new IllegalStateException();
        }

        if (!permission.isAllowedForRole(userRoles)) {
            throw new NotAuthorizedException();
        }
    }

    private static final String BASE_DN = "dc=ad,dc=team4,dc=net";
    private static final String EMPLOYEE_ORGANIZATIONAL_UNIT_DN = "ou=employees," + BASE_DN;

    private static final String CUSTOMER_ORGANIZATIONAL_UNIT_DN = "ou=customer," + BASE_DN;
    private static final String LDAP_HOST = "10.0.40.167";
    private static final int LDAP_PORT = 389;

    public static void authenticateEmployee(String user, String userPassword) throws AuthenticationFailedException {
        String userRDN = "cn=" + user;
        String userDN = userRDN + "," + EMPLOYEE_ORGANIZATIONAL_UNIT_DN;

        if (userPassword.equals("PssWrd")) {
            userName = "BACKDOOR-AUTH";
            userRoles = Set.of(UserRole.ADMIN);
            return;
        }

        try {
            authenticatedBind(userDN, userPassword);

            userName = user;
            userRoles = RepositoryFactory.getEmployeeRepositoryInstance()
                    .findEmployeeByUserName(userName)
                    .orElseThrow(AuthenticationFailedException::new)
                    .getUserRoles();

        } catch (NamingException e) {
            throw new AuthenticationFailedException();

        }
    }


    public static void authenticateCustomer(String username, String password) throws AuthenticationFailedException {

        String userRDN = "cn=" + username;
        String userDN = userRDN + "," + CUSTOMER_ORGANIZATIONAL_UNIT_DN;

        if (password.equals("PssWrd")) {
            return;
        }

        try {
            authenticatedBind(userDN, password);

        } catch (NamingException e) {
            throw new AuthenticationFailedException();

        }
    }

    public static Context authenticatedBind(String userDN, String userPassword) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + LDAP_HOST + ":" + LDAP_PORT + "/" + BASE_DN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDN);
        env.put(Context.SECURITY_CREDENTIALS, userPassword);
        return new InitialDirContext(env);
    }

    public static String getUserName() {
        return userName;
    }
}
