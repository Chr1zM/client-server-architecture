package crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;

public class Crypto {

    public static void genKeyPair(String name) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PublicKey pub = pair.getPublic();
        PrivateKey priv = pair.getPrivate();

        writeKeyFile(pub, name + ".pub");
        writeKeyFile(priv, name + ".priv");
    }

    public static void writeKeyFile(Key key, String filename) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(key);
        }
    }

    public static Key readKeyFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Key) inputStream.readObject();
        }
    }

    public static byte[] encrypt(byte[] msg, PublicKey pub)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pub);
        cipher.update(msg);
        return cipher.doFinal();
    }

    public static byte[] decrypt(byte[] encrypted, PrivateKey priv)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priv);
        cipher.update(encrypted);
        return cipher.doFinal();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // Erzeuge Key Pairs für zwei exemplarische User
        genKeyPair("Harry");
        genKeyPair("Harald");
    }
}
