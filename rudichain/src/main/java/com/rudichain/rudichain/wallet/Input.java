package com.rudichain.rudichain.wallet;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Map;

import org.web3j.crypto.Sign;

public class Input{
    public long timestamp;
    public double amount;
    public BigInteger address;
    public Sign.SignatureData signature;

    Input(Wallet senderWallet, Map<String,Double> OutputMap){
        this.timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
        this.amount = senderWallet.balance;
        this.address = senderWallet.pubKey;
        this.signature = senderWallet.sign(OutputMap);
    }

    // public Input(String address){
    //     this.timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
    //     this.amount = senderWallet.balance;
    //     this.address = senderWallet.pubKey;
    //     this.signature = senderWallet.sign(OutputMap);
    // }

    public Input(){
        this.timestamp = 0;
        this.amount = 0;
        this.address = null;
        this.signature = null;
    }

}