package com.rudichain.rudichain.backend;

import com.rudichain.rudichain.constants;
import com.rudichain.rudichain.cryptography.ECDSA;
import com.rudichain.rudichain.cryptography.Hash;
import com.rudichain.rudichain.wallet.Transaction;
import com.rudichain.rudichain.wallet.Wallet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson; 

public class blockchain implements constants{
  public ArrayList<block> chain;

  public blockchain(){
    this.chain = new ArrayList<block>();
    chain.add(GENISIS_BLOCK);
  }

  public blockchain(ArrayList<block> chain){
    this.chain = chain;
  }

  public void addBlock(ArrayList<Transaction> data){
    final block newBlock = block.mineBlock(this.chain.get(this.chain.size()-1),data);
    this.chain.add(newBlock);
  }

  static boolean isValidChain(blockchain current){

    //checking if they have the same genesis block
    Gson gson = new Gson();
    String json = gson.toJson(current.chain.get(0));
    String genesis_json = gson.toJson(GENISIS_BLOCK);
    if(!(json.equals(genesis_json))) return false;

    for(int i=1; i<current.chain.size(); i++){

      //checking for last hash of curr block and hash of prev block
      if(!current.chain.get(i-1).hash.equals(current.chain.get(i).lastHash)) return false;

      //checking if the hash is accurate
      String accurateHash = Hash.SHA256(current.chain.get(i).lastHash, 
                                        current.chain.get(i).data, 
                                        current.chain.get(i).timestamp, 
                                        current.chain.get(i).nonce, 
                                        current.chain.get(i).difficulty
                                        );

      if(!(current.chain.get(i).hash.equals(accurateHash))) return false;

      //checking for the difficulty difference
      if (Math.abs(current.chain.get(i-1).difficulty - current.chain.get(i).difficulty) > 1) return false;
    }

    return true;
  }

  public boolean replaceChain(blockchain toReplace){

    if(toReplace.chain.size()<this.chain.size()) return false;
    
    if(!isValidChain(toReplace)) return false;

    if(!this.hasValidTransactionData(toReplace)) return false;

    this.chain = toReplace.chain;

    Gson gson = new Gson();
    System.out.println(gson.toJson(toReplace.chain));
    System.out.println("The incoming chain replaced the original one");
    return true;
  }

  public boolean hasValidTransactionData(blockchain newChain){
    for(int i=1; i<newChain.chain.size(); i++){
      block obj = newChain.chain.get(i);
      Set<Transaction> transactionSet = new HashSet<>();
      int countRewards = 0;
      if(obj.data.size()==1){
        System.err.println("Mined zero transactions, Invalid block!!");
        return false;
      }
      for(Transaction x: obj.data){
        if(x.input == null){
          countRewards++;
          if(countRewards>1){
            System.err.println("More than one null input in transaction");
            return false;
          }
          if(x.OutputMap.values().iterator().next() != MINING_REWARD){
            System.err.println("Mining reward doesn't match");
            return false;
          }
        }else{
          if(!Transaction.validTransaction(x)){
            System.err.println("Invalid Transaction!");
            return false;
          }
          //......................................................................................
          //this below condition needs to be tested for various scenarios
          //......................................................................................
          // double realBalance = Wallet.calcBalance(this, ECDSA.compressPubKey(x.input.address));
          // if(x.input.amount!=realBalance){
          //   System.err.println("Invalid amount in Transaction");
          //   return false;
          // }
          //......................................................................................
          if(transactionSet.contains(x)){
            System.err.println("Identical transactions!!");
            return false;
          }else{
            transactionSet.add(x);
          }
        }
      }
    }
    return true;
  }

}