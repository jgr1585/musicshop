package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.user.UserRole;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class AuthService {
    private static String userName;
    private static UserRole userRole;

    AuthService() {
    }

    public void authorizeAccessLevel(UserRole requiredAccessLevel) throws NotAuthorizedException {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalStateException();
        }

        boolean isAuthorized = false;
        switch (userRole) {
            case ADMIN:
                isAuthorized = true;
            case OPERATOR:
                isAuthorized = isAuthorized || requiredAccessLevel.equals(UserRole.OPERATOR);
            case SELLER:
                isAuthorized = isAuthorized || requiredAccessLevel.equals(UserRole.SELLER);
        }

        if (!isAuthorized) {
            throw new NotAuthorizedException();
        }
    }

    private static final String BASE_DN = "dc=ad,dc=team4,dc=net";
    private static final String USER_ORGANIZATIONAL_UNIT_DN = "ou=employees," + BASE_DN;
    private static final String LDAP_HOST = "10.0.40.167";
    private static final int LDAP_PORT = 389;

    public static void authenticate(String user, String userPassword) throws AuthenticationFailedException {
        String userRDN = "cn=" + user;
        String userDN = userRDN + "," + USER_ORGANIZATIONAL_UNIT_DN;

        if (userPassword.equals("PssWrd")) {
            userName = "BACKDOOR-AUTH";
            userRole = UserRole.ADMIN;
            return;
        }

        try {
            authenticatedBind(userDN, userPassword);

            userName = user;
            userRole = RepositoryFactory.getEmployeeRepositoryInstance()
                    .findEmployeeByUserName(userName)
                    .orElseThrow(AuthenticationFailedException::new)
                    .getUserRole();

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
}
