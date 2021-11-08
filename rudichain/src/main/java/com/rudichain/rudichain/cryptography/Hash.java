package com.rudichain.rudichain.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public final class Hash{

    private Hash(){}

    public static String SHA256(String lastHash, String data, long timestamp, long nonce, int difficulty){
        
        String input = lastHash+data+Long.toString(timestamp)+Long.toString(nonce)+Integer.toString(difficulty);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}