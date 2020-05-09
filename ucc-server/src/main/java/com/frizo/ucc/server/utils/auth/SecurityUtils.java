package com.frizo.ucc.server.utils.auth;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SecurityUtils {
    public static String generateSecurityCode(){
        int sault = new Random().ints(0, 1000000).findAny().getAsInt();
        String key = String.valueOf(sault);
        MessageDigest md = null;
        String result = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            result = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
