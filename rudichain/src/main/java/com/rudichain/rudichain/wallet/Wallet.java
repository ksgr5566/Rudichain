package com.rudichain.rudichain.wallet;

import java.math.BigInteger;
import java.util.Map;
import com.google.gson.Gson; 

import com.rudichain.rudichain.cryptography.ECDSA;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

public class Wallet{

    ECDSA keys;
    double balance;
    public String publicKey;
    BigInteger pubKey; 

    public Wallet(){
        try{
            this.keys = new ECDSA();
            this.pubKey = keys.getPublicKey();
            this.publicKey = keys.getPublic();
        }catch(Exception e){
            System.out.println("Exception caught while creating ECDSA in wallet");
        }
        this.balance = 1000;
    }

    Sign.SignatureData sign(Map<String,Double> OutputMap){
        Gson gson = new Gson();
        String json = gson.toJson(OutputMap);
        byte[] msgHash = Hash.sha3(json.getBytes());
        return Sign.signMessage(msgHash, keys.getKeys(), false);
    }

    public Transaction createTransaction(String recipient, double amount) throws InvalidTransaction{
        if(amount > this.balance){
            throw new InvalidTransaction("Amount exceeds Balance!");
        }

        return new Transaction(this, recipient, amount);
    }

}
