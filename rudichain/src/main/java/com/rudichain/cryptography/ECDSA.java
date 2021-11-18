package com.rudichain.cryptography;

import org.web3j.crypto.*;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.gson.Gson;

public class ECDSA{

    public static ECKeyPair keyPair;
    
    public String getPublic(){
        return compressPubKey(keyPair.getPublicKey());
    }

    public BigInteger getPublicKey(){
        return keyPair.getPublicKey();
    }

    public ECKeyPair getKeys(){
        return keyPair;
    }
    
    public ECDSA(){
        try{
            BigInteger privKey = Keys.createEcKeyPair().getPrivateKey();
            BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
            keyPair = new ECKeyPair(privKey, pubKey);
        }catch(Exception e){
            System.err.println("Exception caught in ECDSA");
        }
    }

    public ECDSA(String publickey, String privatekey){
        try{
            BigInteger priv = Base58.decodeToBigInteger(privatekey);
            BigInteger pubKey = Sign.publicKeyFromPrivate(priv);
            keyPair = new ECKeyPair(priv, pubKey);
        }catch(AddressFormatException e){
            System.err.println("Address format exception caught!!");
        }

    }

    public static boolean verifySignature(BigInteger pubKey, String data, Sign.SignatureData signature) throws SignatureException{

        String sorted = Stream.of(data.split("")).sorted().collect(Collectors.joining());
        BigInteger pubKeyRecovered = Sign.signedMessageToKey(sorted.getBytes(), signature);
         
        // System.out.println(pubKeyRecovered);
        System.out.println("");
        System.out.println("data in verify signature in ECDSA.java");
        System.out.println(sorted);
        System.out.println("");
        System.out.println("");

        return pubKey.equals(pubKeyRecovered);
    }

    public static String compressPubKey(BigInteger pubKey) {
        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
        String pubKeyHex = pubKey.toString(16);
        String pubKeyX = pubKeyHex.substring(0, 64);
        return pubKeyYPrefix + pubKeyX;
    }

    public static String privateKey(){
        return Base58.encode(keyPair.getPrivateKey().toByteArray());
    }

    public static boolean keyCheck(String publicKey, String privateKey){
        try{
            BigInteger priv = Base58.decodeToBigInteger(privateKey);
            if(compressPubKey(Sign.publicKeyFromPrivate(priv)).equals(publicKey)) return true;
        }catch(AddressFormatException e){
            System.err.println("Address format exception caught!!");
        }
        return false;
    }

}