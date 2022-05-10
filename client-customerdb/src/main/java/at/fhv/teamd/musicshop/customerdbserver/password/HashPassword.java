package at.fhv.teamd.musicshop.customerdbserver.password;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class HashPassword {

    public static boolean checkPassword(String password, byte[] hash, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);
        SecretKeyFactory factory;
        byte[] checkhash;

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            checkhash = factory.generateSecret(spec).getEncoded();

            return Arrays.equals(checkhash, hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
