package com.cinema.cinema;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Per gestire tutto ciò che riguarda la crittografia
public class Cypherer {

    public static String toMD5(String str)
    {
        MessageDigest md;
        StringBuilder sb = new StringBuilder();

        try {
            md = MessageDigest.getInstance("MD5"); // Permette di sfruttare diversi algoritmi di crittografia, a noi serv md5
            byte[] messageDigest = md.digest(str.getBytes()); // Crypto i singoli byte della stringa
            
            // Rappresento i singoli byte in hex
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
        }
        } catch (NoSuchAlgorithmException e) {
            // Da vedere
            e.printStackTrace();
        }

        return sb.toString();
    }

    //(SHA-256)
    public static String hash(String str)
    {
        MessageDigest digest;
        StringBuilder sb = new StringBuilder();

        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            sb = new StringBuilder(2 * encodedhash.length);
            for(byte b : encodedhash) {
                sb.append(String.format("%02x", b));
        }
        } catch (NoSuchAlgorithmException e) {
            // Da vedere
            e.printStackTrace();
        }

        return sb.toString();
    }
}
