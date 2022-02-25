package com.example;

import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExampleTest {
    @Test
    public void test() {
        System.out.println("test");

        try {
            InputStream in = null;
            byte[] cipherData = null;

            in = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/test.licence");
            final int len = in.available();
            cipherData = new byte[len];
            in.read(cipherData);

            InputStream pubIn;
            // pubIn = ExampleTest.class.getClassLoader().getResourceAsStream("public.crt");
            pubIn = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/public.crt");
            PublicKey pubKey = getPublicKey(ReadTest.read(pubIn));

            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, pubKey);

            byte[] bytes = cipher.doFinal(cipherData);// [B@25b485ba
            String result = new String(bytes);
            System.out.println(result);
            /*
            * {
  "code" : "00000000-00000000-00000000-00000000",
  "organization" : "社区版",
  "start" : "2020-01-01",
  "sign" : "63932CC5-E9762317-571074D8-33A8B312",
  "edition" : "社区版",
  "expiry" : "2030-01-01"
}*/
            Map<String, String> resultMap = new HashMap<String, String>();
            final String json = new String(bytes);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

            resultMap.put("code", "00000000-00000000-00000000-00000000");
            resultMap.put("organization", "社区版");
            resultMap.put("start", "2020-01-01");
            resultMap.put("edition", "社区版");
            resultMap.put("expiry", "2030-01-01");

            System.out.println(c(resultMap));// 63932CC5-E9762317-571074D8-33A8B312
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test1() {
        System.out.println("test");

        try {
            InputStream in = null;
            byte[] cipherData = null;

            in = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/test.licence");
            final int len = in.available();
            cipherData = new byte[len];
            in.read(cipherData);

            InputStream pubIn;
            // pubIn = ExampleTest.class.getClassLoader().getResourceAsStream("public.crt");
            pubIn = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/public.crt");
            PublicKey pubKey = getPublicKey(ReadTest.read(pubIn));

            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, pubKey);

            byte[] bytes = cipher.doFinal(cipherData);// [B@25b485ba
            String result = new String(bytes);
            System.out.println(result);
            /*
            * {
  "code" : "00000000-00000000-00000000-00000000",
  "organization" : "社区版",
  "start" : "2020-01-01",
  "sign" : "63932CC5-E9762317-571074D8-33A8B312",
  "edition" : "社区版",
  "expiry" : "2030-01-01"
}*/
            Map<String, String> resultMap = new HashMap<String, String>();
            final String json = new String(bytes);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

            resultMap.put("code", "00000000-00000000-00000000-00000000");
            resultMap.put("organization", "社区版");
            resultMap.put("start", "2020-01-01");
            resultMap.put("edition", "社区版");
            resultMap.put("expiry", "2030-01-01");

            System.out.println(c(resultMap));// 63932CC5-E9762317-571074D8-33A8B312
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private static String c(final Map<String, String> map) {
        final String sign = EncryptUtil.n(map.toString(), "").replaceAll("(.{8})", "$1-").toUpperCase();
        return sign.substring(0, sign.length() - 1);
    }

    public static PublicKey getPublicKey(final String pubKeyBase64) throws Exception {
        final byte[] encPubKey = Base64.getDecoder().decode(pubKeyBase64);
        final X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
        return KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
    }
}
