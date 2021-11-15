package com.rudichain.rudichain.cryptography;

import com.rudichain.rudichain.wallet.Transaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public final class Hash{

    private Hash(){}

    public static String SHA256(String lastHash, ArrayList<Transaction> data, long timestamp, long nonce, int difficulty){

        String jsonData = new Gson().toJson(data);
        
        String input = lastHash+jsonData+Long.toString(timestamp)+Long.toString(nonce)+Integer.toString(difficulty);
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