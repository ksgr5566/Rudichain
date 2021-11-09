package com.rudichain.rudichain.cryptography;

import org.web3j.crypto.*;
import java.math.BigInteger;
import java.security.SignatureException;

public class ECDSA{

    private ECKeyPair keyPair;
    
    public String getPublic(){
        return compressPubKey(keyPair.getPublicKey());
    }

    public BigInteger getPublicKey(){
        return keyPair.getPublicKey();
    }

    public ECKeyPair getKeys(){
        return keyPair;
    }
    
    public ECDSA() throws Exception{
        BigInteger privKey = Keys.createEcKeyPair().getPrivateKey();
        BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
        this.keyPair = new ECKeyPair(privKey, pubKey);
    }

    public static boolean verifySignature(BigInteger pubKey, String data, Sign.SignatureData signature) throws SignatureException{
        BigInteger pubKeyRecovered = Sign.signedMessageToKey(data.getBytes(), signature);
        return pubKey.equals(pubKeyRecovered);
    }

    public static String compressPubKey(BigInteger pubKey) {
        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
        String pubKeyHex = pubKey.toString(16);
        String pubKeyX = pubKeyHex.substring(0, 64);
        return pubKeyYPrefix + pubKeyX;
    }

}