package com.rudichain.rudichain.wallet;

import java.math.BigInteger;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.web3j.crypto.Sign;

import com.google.gson.Gson;
import com.rudichain.rudichain.cryptography.ECDSA;

public class Transaction{

    Map<String,Double> OutputMap;
    Input input;
    String id;

    //, Map<String,Double> outputMap, Map<String,Object> Input

    Transaction(Wallet senderWallet, String recipient, double amount){
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        createOutputMap(senderWallet, recipient, amount);
        input = new Input(senderWallet, OutputMap);
    }

    void createOutputMap(Wallet senderWallet, String recipient, double amount){
        Map<String,Double> map = new HashMap<>();
        map.put(recipient, amount);
        map.put(senderWallet.publicKey, senderWallet.balance - amount);
        this.OutputMap = map;     
    }

    Map<String,Double> createMap(Wallet senderWallet, String recipient, double amount){
        Map<String,Double> map = new HashMap<>();
        map.put(recipient, amount);
        map.put(senderWallet.publicKey, senderWallet.balance - amount);
        return map;     
    }

    static boolean validTransaction(Transaction transaction){

        double outputTotal = 0;
        for (Double val : transaction.OutputMap.values()){
            outputTotal += val;
        }
        
        if(transaction.input.amount != outputTotal){
            System.out.println("Invalid transaction: Amount doesn't match ->" + ECDSA.compressPubKey(transaction.input.address));
            return false;
        }

        Gson gson = new Gson();
        String data = gson.toJson(transaction.OutputMap);

        try{
            if(!ECDSA.verifySignature(transaction.input.address, data, transaction.input.signature)){
                System.out.println("Invalid transaction: Unverified Signature  ->" + ECDSA.compressPubKey(transaction.input.address));
                return false;
            }
        }catch(SignatureException e){
            System.out.println("Exception caught at verifySignature in Transaction.java");
            e.printStackTrace();
        }

        return true;
    }

    void update(Wallet senderWallet, String recipient, double amount) throws InvalidTransaction{
        if(amount > this.OutputMap.get(senderWallet.publicKey)){
            throw new InvalidTransaction("Amount exceeds Balance!");
        }

        this.OutputMap = createMap(senderWallet, recipient, amount);
        //........................................................
        double k=0;
        if(!this.OutputMap.containsKey(recipient)){
            this.OutputMap.replace(recipient, amount);
        }else{
            k = this.OutputMap.get(recipient);
            this.OutputMap.replace(recipient, k+amount);
        }
        k = this.OutputMap.get(senderWallet.publicKey);
        this.OutputMap.replace(senderWallet.publicKey, k-amount);
        //.......................................................
        this.input = new Input(senderWallet, OutputMap);
    }
    
}

class Input{
    long timestamp;
    double amount;
    BigInteger address;
    Sign.SignatureData signature;

    Input(Wallet senderWallet, Map<String,Double> OutputMap){
        this.timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
        this.amount = senderWallet.balance;
        this.address = senderWallet.pubKey;
        this.signature = senderWallet.sign(OutputMap);
    }


}