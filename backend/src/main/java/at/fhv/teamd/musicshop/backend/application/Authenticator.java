package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class Authenticator {
    private static final String baseDN = "dc=ad,dc=team4,dc=net";
    private static final String userOrganizationalUnitDN = "ou=employees," + baseDN;
    private static final String LDAP_HOST = "10.0.40.167";
    private static final int LDAP_PORT = 389;

    public static void authenticate(String user, String userPassword) throws AuthenticationFailedException {
        String userRDN = "cn=" + user;
        String userDN = userRDN + "," + userOrganizationalUnitDN;
        try {
            authenticatedBind(userDN, userPassword);

        } catch (NamingException e) {
            throw new AuthenticationFailedException();

        }
    }

    public static Context authenticatedBind(String userDN, String userPassword) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + LDAP_HOST + ":" + LDAP_PORT + "/" + baseDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDN);
        env.put(Context.SECURITY_CREDENTIALS, userPassword);
        return new InitialDirContext(env);
    }
}
