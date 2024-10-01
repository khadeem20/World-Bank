package db_objs;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryptor {

    public static final SecretKey secretKey = generateSecretKey();

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

    // Generate an AES secret key
    public static SecretKey generateSecretKey(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // AES key size (128, 192, or 256 bits)
            return keyGenerator.generateKey();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
