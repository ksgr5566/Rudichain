package com.rudichain.rudichain.wallet;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.rudichain.rudichain.cryptography.ECDSA;

public class TransactionPool {
    public Map<String,Transaction> transactionMap = new HashMap<>();

    public TransactionPool(){
        transactionMap = new HashMap<>();
    }

    public void setTransaction(Transaction transaction){
        this.transactionMap.put(transaction.id, transaction);
    }

    public Transaction exists(String address){

        for (Map.Entry<String, Transaction> set : transactionMap.entrySet()) {
            BigInteger k = set.getValue().input.address;
            String x = ECDSA.compressPubKey(k);
		    if(x.equals(address)){
                return set.getValue();
            }
		}

        return null;
    }

}
