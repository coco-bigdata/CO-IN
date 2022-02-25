package cn.example.framework.util.user;

import cn.example.application.entity.user.*;
import java.net.*;
import cn.example.framework.util.command.*;
import java.math.*;
import cn.example.framework.util.encrypt.*;
import java.security.spec.*;
import javax.crypto.*;
import java.security.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import com.fasterxml.jackson.core.type.*;
import java.util.*;
import java.text.*;

public class b
{
    private static final String ALGORITHM = "RSA";
    private static final int cY = 2048;
    private static final String regex = "(.{8})";
    private static final String PATH;
    private static final String salt = "examplespace.com";
    private static final String personal = "dacbaf863df5baf6d5905fb1cb1b2cca";
    private static final String cZ = "582f2372ca70788b56f18078111d466b";
    private static final String enterprise = "833763880d94595303c032e46591fb91";

    public static List<SystemPerm> b(final List<SystemPerm> list) {
        final Vector<SystemPerm> result = new Vector<SystemPerm>();
        if (null == list || list.size() == 0) {
            return result;
        }
        final Iterator<SystemPerm> iterator = list.iterator();
        while (iterator.hasNext()) {
            final SystemPerm temp = iterator.next();
            if (null == temp.getPid() || 0L == temp.getPid()) {
                result.add(temp);
                iterator.remove();
            }
        }
        for (final SystemPerm tp : result) {
            a(tp, list);
        }
        return result;
    }

    private static void a(final SystemPerm perm, final List<SystemPerm> list) {
        perm.setChildren((List)new Vector());
        perm.setExpand(Boolean.valueOf(false));
        final Iterator<SystemPerm> iterator = list.iterator();
        while (iterator.hasNext()) {
            final SystemPerm temp = iterator.next();
            if (null != temp.getPid() && null != perm.getId() && temp.getPid().equals(perm.getId())) {
                perm.getChildren().add(temp);
                if (!temp.isApi()) {
                    perm.setExpand(Boolean.valueOf(true));
                }
                iterator.remove();
            }
        }
        for (final SystemPerm tp : perm.getChildren()) {
            a(tp, list);
        }
    }

    public static List<String> getMacAddress() {
        final List<String> result = new ArrayList<String>();
        try {
            final Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            byte[] mac = null;
            while (allNetInterfaces.hasMoreElements()) {
                final NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (!netInterface.isLoopback() && !netInterface.isVirtual() && !netInterface.getName().startsWith("docker") && !netInterface.getName().startsWith("lo")) {
                    if (netInterface.getName().startsWith("uengine")) {
                        continue;
                    }
                    mac = netInterface.getHardwareAddress();
                    if (mac == null) {
                        continue;
                    }
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; ++i) {
                        if (i != 0) {
                            sb.append(":");
                        }
                        final String s = Integer.toHexString(mac[i] & 0xFF);
                        sb.append((s.length() == 1) ? (0 + s) : s);
                    }
                    if (sb.length() <= 0) {
                        continue;
                    }
                    result.add(sb.toString());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getHostName() {
        final List<String> result = (List<String>)a.a("hostname", new String[0]);
        if (null != result && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public static String md5(final String plainText) {
        byte[] secretBytes = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            secretBytes = md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); md5code = "0" + md5code, ++i) {}
        return md5code;
    }

    public static String getMachineCode() {
        final Set<String> result = new HashSet<String>();
        final List<String> macs = getMacAddress();
        final Properties props = System.getProperties();
        final String javaVersion = props.getProperty("java.version");
        final String vmVersion = props.getProperty("java.vm.version");
        final String osVersion = props.getProperty("os.version");
        final String hostname = getHostName();
        result.addAll(macs);
        result.add(javaVersion);
        result.add(vmVersion);
        result.add(osVersion);
        result.add(hostname);
        final String regex = "(.{8})";
        final String code = EncryptUtil.n(result.toString(), "").replaceAll(regex, "$1-").toUpperCase();
        return code.substring(0, code.length() - 1);
    }

    public static PublicKey getPublicKey(final String pubKeyBase64) throws Exception {
        final byte[] encPubKey = Base64.getDecoder().decode(pubKeyBase64);
        final X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
        return KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
    }

    public static byte[] a(final byte[] cipherData, final PublicKey pubKey) throws Exception {
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, pubKey);
        return cipher.doFinal(cipherData);
    }

    private static byte[] a(final byte[] cipherData, final InputStream in) throws Exception {
        final PublicKey pubKey = getPublicKey(cn.example.framework.util.user.a.read(in));
        final byte[] plainData = a(cipherData, pubKey);
        return plainData;
    }

    private static String c(final Map<String, String> map) {
        final String sign = EncryptUtil.n(map.toString(), "").replaceAll("(.{8})", "$1-").toUpperCase();
        return sign.substring(0, sign.length() - 1);
    }

    public static boolean d(final Map<String, String> map) {
        if (null == map) {
            return false;
        }
        final String temp_getted = map.get("sign");
        map.remove("sign");
        final String temp_caled = c(map);
        return temp_getted.equals(temp_caled);
    }

    public static Map<String, String> R() {
        InputStream in = null;
        Map<String, String> mp = null;
        byte[] cipherData = null;
        final String machineCode = getMachineCode();
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            try {
                final File f = new File(b.PATH + File.separator + "test.licence");
                if (!f.exists()) {
                    mp = new HashMap<String, String>();
                    mp.put("code", machineCode);
                    mp.put("status", "False");
                    mp.put("message", "\u6ca1\u6709\u6388\u6743\u6587\u4ef6");// 没有授权文件
                    return mp;
                }
                in = new FileInputStream(b.PATH + File.separator + "test.licence");
                final int len = in.available();
                cipherData = new byte[len];
                in.read(cipherData);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                mp = new HashMap<String, String>();
                mp.put("code", machineCode);
                mp.put("status", "False");
                mp.put("message", "\u6ca1\u6709\u6388\u6743\u6587\u4ef6");// 没有授权文件
                return mp;
            }
            catch (IOException e2) {
                e2.printStackTrace();
                mp = new HashMap<String, String>();
                mp.put("code", machineCode);
                mp.put("status", "False");
                mp.put("message", "\u6ca1\u6709\u6388\u6743\u6587\u4ef6");// 没有授权文件
                return mp;
            }
            finally {
                try {
                    if (null != in) {
                        in.close();
                    }
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            final InputStream pubIn = b.class.getClassLoader().getResourceAsStream("licence/public.crt");
            byte[] plainData = new byte[0];
            try {
                plainData = a(cipherData, pubIn);
            }
            catch (Exception e4) {
                e4.printStackTrace();
            }
            final String result = new String(plainData);
            try {
                final TypeReference<Map<String, String>> type = (TypeReference<Map<String, String>>)new b.b$1();
                mp = (Map<String, String>)mapper.readValue(result, (TypeReference)type);
                final boolean status = d(mp);
                if (!status) {
                    mp.put("code", machineCode);
                    mp.put("status", "False");
                    mp.put("message", "\u6388\u6743\u6587\u4ef6\u7b7e\u540d\u9519\u8bef");// 授权文件签名错误
                    return mp;
                }
                if (h(mp)) {
                    return null;
                }
                if (!machineCode.equals(mp.get("code"))) {
                    mp.put("status", "False");
                    mp.put("message", "\u6388\u6743\u6587\u4ef6Code\u4e0d\u4e00\u81f4:" + mp.get("code"));// 授权文件Code不一致
                    mp.put("code", machineCode);
                    return mp;
                }
                if (g(mp)) {
                    return null;
                }
                if (!f(mp)) {
                    mp.put("code", machineCode);
                    mp.put("status", "False");
                    mp.put("message", "\u7248\u672c\u4fe1\u606f\u9519\u8bef");// 版本信息错误
                    return mp;
                }
                final Date current = new Date();
                final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final Date start = df.parse(mp.get("start"));
                final Date expiry = df.parse(mp.get("expiry"));
                if (start.getTime() > current.getTime() || expiry.getTime() < current.getTime()) {
                    mp.put("status", "False");
                    mp.put("message", "\u6388\u6743\u5df2\u8fc7\u671f");// 授权已过期
                    return mp;
                }
            }
            catch (IOException e5) {
                e5.printStackTrace();
                mp.put("status", "False");
                mp.put("message", "\u6388\u6743\u6587\u4ef6\u9519\u8bef:" + mp.get("code"));// 授权文件错误
                mp.put("code", machineCode);
                return mp;
            }
            catch (ParseException pe) {
                mp.put("status", "False");
                mp.put("message", "\u6388\u6743\u6587\u4ef6\u9519\u8bef:" + mp.get("code"));// 授权文件错误
                mp.put("code", machineCode);
                return mp;
            }
        }
        catch (Exception ee) {
            ee.printStackTrace();
            mp = new HashMap<String, String>();
            mp.put("code", machineCode);
            mp.put("status", "False");
            mp.put("message", "\u6ca1\u6709\u6388\u6743\u6587\u4ef6");// 没有授权文件
            return mp;
        }
        return null;
    }

    public static Map<String, String> ad(final String path) {
        final String machineCode = getMachineCode();
        Map<String, String> result = new HashMap<String, String>();
        InputStream in = null;
        try {
            byte[] cipherData;
            try {
                final File f = new File(path);
                if (!f.exists()) {
                    result.put("code", machineCode);
                    result.put("status", "False");
                    return result;
                }
                in = new FileInputStream(path);
                final int len = in.available();
                cipherData = new byte[len];
                in.read(cipherData);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                result.put("code", machineCode);
                result.put("status", "False");
                return result;
            }
            catch (IOException e2) {
                e2.printStackTrace();
                result.put("code", machineCode);
                result.put("status", "False");
                return result;
            }
            finally {
                try {
                    if (null != in) {
                        in.close();
                    }
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            final InputStream pubIn = b.class.getClassLoader().getResourceAsStream("licence/public.crt");
            byte[] plainData = null;
            plainData = a(cipherData, pubIn);
            final String json = new String(plainData);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            final TypeReference<Map<String, String>> type = (TypeReference<Map<String, String>>)new b.b$2();
            result = (Map<String, String>)mapper.readValue(json, (TypeReference)type);
            final boolean vali = d(result);
            if (!vali) {
                result.put("code", machineCode);
                result.put("status", "False");
                return result;
            }
            if (h(result)) {
                result.put("code", machineCode);
                result.put("status", "True");
                return result;
            }
            if (!machineCode.equals(result.get("code"))) {
                result.put("code", machineCode);
                result.put("status", "False");
                return result;
            }
            final Date current = new Date();
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            final Date start = df.parse(result.get("start"));
            final Date expiry = df.parse(result.get("expiry"));
            if (start.getTime() > current.getTime() || expiry.getTime() < current.getTime()) {
                result.put("code", machineCode);
                result.put("status", "False");
                return result;
            }
            result.put("status", "True");
            return result;
        }
        catch (Exception ee) {
            ee.printStackTrace();
            result.clear();
            result.put("code", machineCode);
            result.put("status", "False");
            return result;
        }
    }

    public static Map<String, String> getLicenceInfo() {
        final String path = b.PATH + File.separator + "test.licence";
        return ad(path);
    }

    public static boolean e(final Map<String, String> info) {
        return h(info) || g(info) || f(info);
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

    static {
        PATH = cn.example.framework.util.yml.a.get("example.conf");
    }
}
