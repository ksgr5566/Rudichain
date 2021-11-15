package com.rudichain.rudichain.wallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.rudichain.rudichain.cryptography.ECDSA;
import com.rudichain.rudichain.backend.*;

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

    public ArrayList<Transaction> validTransactions(){
        List<Transaction> temp = this.transactionMap.entrySet().stream().filter(map -> Transaction.validTransaction(map.getValue())).map(map->map.getValue()).collect(Collectors.toList());
        return new ArrayList<Transaction>(temp);
    }

    public void clear(){
        this.transactionMap.clear();
    }

    public void clearBlockchainTransactions(blockchain chain){
        for(int i=1; i<chain.chain.size(); i++){
            block obj = chain.chain.get(i);
            for(Transaction v: obj.data){
                this.transactionMap.remove(v.id);
            }
        }
    }

}