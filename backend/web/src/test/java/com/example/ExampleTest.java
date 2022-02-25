package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ExampleTest {
    @Test
    public void test() {
        System.out.println("test");

        try {
            InputStream in = null;
            byte[] cipherData = null;

            ObjectMapper mapper = new ObjectMapper();
            in = new FileInputStream("test.licence");
            final int len = in.available();
            cipherData = new byte[len];
            in.read(cipherData);

            InputStream pubIn = ExampleTest.class.getClassLoader().getResourceAsStream("public.crt");
            PublicKey pubKey = getPublicKey(ReadTest.read(pubIn));

            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, pubKey);

            System.out.println(cipher.doFinal(cipherData));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static PublicKey getPublicKey(final String pubKeyBase64) throws Exception {
        final byte[] encPubKey = Base64.getDecoder().decode(pubKeyBase64);
        final X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
        return KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
    }
}
