package com.example;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.File;
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
            System.out.println("len=" + len);
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
            Map<String, String> resultMap = new HashMap<String, String>();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

            resultMap.put("code", "00000000-00000000-00000000-00000000");
            resultMap.put("organization", "商业版");
            resultMap.put("start", "2020-01-01");
            resultMap.put("edition", "商业版");
            resultMap.put("expiry", "2030-01-01");

            String sign = c(resultMap);// 8CAE88E0-F70DC710-2988BA2E-B07CE5C5
            System.out.println(sign);
            resultMap.put("sign", sign);

            String data = "";
            data = "{\n" +
                    "  \"code\" : \"00000000-00000000-00000000-00000000\",\n" +
                    "  \"organization\" : \"社区版\",\n" +
                    "  \"start\" : \"2020-01-01\",\n" +
                    "  \"sign\" : \"63932CC5-E9762317-571074D8-33A8B312\",\n" +
                    "  \"edition\" : \"社区版\",\n" +
                    "  \"expiry\" : \"2030-01-01\"\n" +
                    "}";
            data = JSONObject.toJSONString(resultMap);
            //{"code":"00000000-00000000-00000000-00000000","organization":"商业版","start":"2020-01-01","sign":"8CAE88E0-F70DC710-2988BA2E-B07CE5C5","edition":"商业版","expiry":"2030-01-01"}
            System.out.println(data);

            InputStream key = null;
            key = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/private1.key");

            int length;
            byte[] bytes = new byte[1024];
            StringBuilder privateKey = new StringBuilder();
            while ((length = key.read(bytes)) != -1) {
                privateKey.append(new String(bytes, 0, length));//将数据变为字符串输出
            }
            key.close();//关闭流
            // System.out.println(privateKey);

            byte[] dataBytes = data.getBytes();
            byte[] encodedData = RSAUtils.encryptByPrivateKey(dataBytes, privateKey.toString());
            System.out.println("加密后：\r\n" + new String(encodedData)); //加密后乱码是正常的

            System.out.println("len=" + encodedData.length);

            Base64Utils.byteArrayToFile(encodedData, System.getProperty("user.dir") + "/src/test/java/com/example/test1.licence");

            InputStream in = null;
            byte[] cipherData = null;

            in = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/test1.licence");
            final int len = in.available();
            System.out.println("len=" + len);
            cipherData = new byte[len];
            in.read(cipherData);

            InputStream pubIn;
            // pubIn = ExampleTest.class.getClassLoader().getResourceAsStream("public.crt");
            pubIn = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/example/public1.crt");
            PublicKey pubKey = getPublicKey(ReadTest.read(pubIn));

            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, pubKey);

            byte[] bytes1 = cipher.doFinal(cipherData);
            String result = new String(bytes1);
            System.out.println(result);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        Map<String, String> resultMap = new HashMap<String, String>();
        String edition = "社区版";
        resultMap.put("edition", edition);
        String result;
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// f0b5cd928354ad37faa2f2f3cba6225b

        edition = "个人版";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// f01ec0c9d6e36e8a07eb4b1ce834c295

        edition = "商业版";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// c83892ec3bf24443fa16117f02e34a82

        edition = "企业版";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 267d6bc967f10a9be27ea9834f468371

        edition = "商业用户";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// d3895bc24d92a39eb7dc4c9d6eaf81e9

        edition = "试用版";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 0b441b97f220a44a9c45a02fca21b996

        edition = "正式版";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 07714e8905df5343dce19ace458d6965

        edition = "personal";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// da1cfc84d3cb12b445bb848ee52835db

        edition = "专业版";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 92f1f6f3b5e49ff23beca1734d934043

        edition = "个人";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 5f6de6de07b7be68110396412a0b9dea

        edition = "企业";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 3cf78355181b38d4870af86f59ba1704

        edition = "组织";//个人 企业 组织
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 690fb46f8c2424e1c8530b3def2f4a5c

        edition = "个人授权";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 3869391e35a7db453c74b86c60fc2507

        edition = "SliverWorkspace";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 60cb932bbd83479019b67c40614da3d4

        edition = "社区版 2.0.375427";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 04cc93ef34c98c3701021aec9fa390c4

        edition = "SliverWorkspace 2.0.375427";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// e993c44f452d17143aa2ad373625c9d8

        edition = "personal 2.0.375427";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// e0773b9c6908f754e54727dccdbb95fa

        edition = "SliverWorkspace-2.0.375427";
        result = EncryptUtil.n(edition, "examplespace.com");
        System.out.println(result);// 4aac1880f0bb7b26db0bbd9f888ca85c
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

    public static boolean f(final Map<String, String> info) {
        final String edition = info.get("edition");
        return null != edition && !"".equals(edition) && EncryptUtil.n(edition, "examplespace.com").equals("833763880d94595303c032e46591fb91");
    }

    public static boolean g(final Map<String, String> info) {
        final String edition = info.get("edition");
        return null != edition && !"".equals(edition) && EncryptUtil.n(edition, "examplespace.com").equals("582f2372ca70788b56f18078111d466b");
    }

    public static boolean h(final Map<String, String> info) {
        final String edition = info.get("edition");
        return null != edition && !"".equals(edition) && EncryptUtil.n(edition, "examplespace.com").equals("dacbaf863df5baf6d5905fb1cb1b2cca");
    }
}
