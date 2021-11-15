package com.rudichain.rudichain.wallet;

import java.math.BigInteger;
import java.util.Map;
import com.google.gson.Gson;
import com.rudichain.rudichain.backend.block;
import com.rudichain.rudichain.backend.blockchain;
import com.rudichain.rudichain.cryptography.ECDSA;
import com.rudichain.rudichain.constants;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

public class Wallet implements constants{

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
        this.balance = STARTING_BALANCE;
    }

    Sign.SignatureData sign(Map<String,Double> OutputMap){
        Gson gson = new Gson();
        String json = gson.toJson(OutputMap);
        byte[] msgHash = Hash.sha3(json.getBytes());
        return Sign.signMessage(msgHash, keys.getKeys(), false);
    }

    public Transaction createTransaction(String recipient, double amount, blockchain chain) throws InvalidTransaction{
        this.balance = calcBalance(chain, this.publicKey);

        if(amount > this.balance){
            throw new InvalidTransaction("Amount exceeds Balance!");
        }

        return new Transaction(this, recipient, amount);
    }

    public static double calcBalance(blockchain chain, String address){
        boolean hasConductedTransaction = false;
        double bal = 0;
        for(int i=chain.chain.size()-1; i>0; i--){
            block obj = chain.chain.get(i);
            for(Transaction var: obj.data){
                if(var.input!=null && ECDSA.compressPubKey(var.input.address).equals(address)){
                    hasConductedTransaction = true;
                }
                if(var.OutputMap.containsKey(address)){
                    bal+=var.OutputMap.get(address);
                }
            }
            if(hasConductedTransaction) break;
        }
        return hasConductedTransaction?bal:STARTING_BALANCE+bal;
    }

}