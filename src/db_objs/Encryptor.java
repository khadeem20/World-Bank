package db_objs;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;
import java.util.Arrays;

public class Encryptor {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_ITERATIONS = 65536;
    private static final int HASH_KEY_LENGTH = 128;

    // Method to hash a string (password) using PBKDF2
    public static String encrypt(String plainText) throws Exception {
        // Generate a salt
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Hash the password with the salt
        byte[] hashedPassword = hashPassword(plainText, salt);

        // Combine salt and hashed password as Base64 string
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashedPassword);
    }

    // Method to validate if the provided password matches the stored hashed password
    public static boolean validatePassword(String plainText, String storedPassword) throws Exception {
        String[] parts = storedPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHash = Base64.getDecoder().decode(parts[1]);

        // Hash the incoming password with the same salt
        byte[] hashOfInput = hashPassword(plainText, salt);

        // Compare the hashes
        return Arrays.equals(storedHash, hashOfInput);
    }

    // Helper method to hash a password using PBKDF2 with a salt
    private static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASH_ITERATIONS, HASH_KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }
}
