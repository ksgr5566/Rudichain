package com.rudichain.wallet;

import java.util.ArrayList;

import com.rudichain.backend.blockchain;
import com.rudichain.network.pubsub;

public class TransactionMiner {
    blockchain chain;
    pubsub client;
    TransactionPool pool;
    Wallet wallet;

    public TransactionMiner(blockchain chain, pubsub client, TransactionPool pool, Wallet wallet){
        this.chain = chain;
        this.client = client;
        this.pool = pool;
        this.wallet = wallet;
    }

    public void mineTransactions(){
        ArrayList<Transaction> temp = this.pool.validTransactions();
        if(temp.isEmpty()){
            System.err.println("Cannot mine when there are no transactions!!!");
            return;
        }
        temp.add(Transaction.rewardTransaction(this.wallet));
        this.chain.addBlock(temp);
        this.client.broadcastChain();
        this.pool.clear();
        this.wallet.balance = Wallet.calcBalance(chain, wallet.publicKey);
    }
}
