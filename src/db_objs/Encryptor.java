package db_objs;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Base64;

public class Encryptor {

    private static final String KEY_FILE = "secret.key";

    // Method to encrypt a string using AES
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        // Return the encrypted string as Base64 (to make it printable)
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt a string using AES
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes);
    }

    // Generate and store an AES secret key, or retrieve it from a file if already stored
    public static SecretKey getSecretKey() {
        File keyFile = new File(KEY_FILE);
        if (!keyFile.exists()) {
            try {
                // Generate new secret key
                SecretKey secretKey = generateSecretKey();

                // Save the key to a file
                try (FileOutputStream keyOut = new FileOutputStream(KEY_FILE)) {
                    keyOut.write(secretKey.getEncoded());
                }

                return secretKey;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Read the saved key from file
                byte[] keyBytes = new byte[16]; // AES-128 uses 16 bytes key
                try (FileInputStream keyIn = new FileInputStream(KEY_FILE)) {
                    keyIn.read(keyBytes);
                }
                return new SecretKeySpec(keyBytes, "AES");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Generate an AES secret key
    public static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // AES key size (128, 192, or 256 bits)
            return keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
