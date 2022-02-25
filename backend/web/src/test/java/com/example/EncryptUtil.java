package com.example;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

public class EncryptUtil {
    public static int cA;
    public static String cB;

    public static String P() {
        return UUID.randomUUID().toString();
    }

    public static String n(final String pass, final String salt) {
        final ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
        final Object obj = new SimpleHash(EncryptUtil.cB, (Object)pass, (Object)credentialsSalt, EncryptUtil.cA);
        return obj.toString();
    }

    public static void main(final String[] args) {
        final String uuid = P();
        System.out.println(uuid);
        System.out.println(n("test", uuid));
    }

    static {
        EncryptUtil.cA = 9;
        EncryptUtil.cB = "MD5";
    }
}
