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
    public final String publicKey = keys.getPublic();
    final BigInteger pubKey = keys.getPublicKey();

    public Wallet() throws Exception{
        this.keys = new ECDSA();
        balance = 0;
    }

    Sign.SignatureData sign(Map<String,Double> OutputMap){
        Gson gson = new Gson();
        String json = gson.toJson(OutputMap);
        byte[] msgHash = Hash.sha3(json.getBytes());
        return Sign.signMessage(msgHash, keys.getKeys(), false);
    }

    Transaction createTransaction(String recipient, double amount) throws InvalidTransaction{
        if(amount > this.balance){
            throw new InvalidTransaction("Amount exceeds Balance!");
        }

        return new Transaction(this, recipient, amount);
    }

}

class InvalidTransaction extends Exception {  
    public InvalidTransaction(String errorMessage) {  
    super(errorMessage);  
    }  
}  