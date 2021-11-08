package com.rudichain.rudichain.backend;

import com.rudichain.rudichain.cryptography.Hash;

import java.util.ArrayList;
import com.google.gson.Gson; 

public class blockchain{
  public ArrayList<block> chain;

  public blockchain(){
    this.chain = new ArrayList<block>();
    chain.add(block.genesis());
  }

  public blockchain(ArrayList<block> chain){
    this.chain = chain;
  }

  public void addBlock(String data){
    final block newBlock = block.mineBlock(this.chain.get(this.chain.size()-1),data);
    this.chain.add(newBlock);
  }

  static boolean isValidChain(blockchain current){

    //checking if they have the same genesis block
    Gson gson = new Gson();
    String json = gson.toJson(current.chain.get(0));
    String genesis_json = gson.toJson(block.genesis());
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

  public void replaceChain(blockchain toReplace){

    if(toReplace.chain.size()<this.chain.size()) return;
    
    if(!isValidChain(toReplace)) return;

    //toadd: validateTransaactions and onSuccess
    
    this.chain = toReplace.chain;


    Gson gson = new Gson();
    System.out.println(gson.toJson(toReplace.chain));

    System.out.println("the incoming chain replaced the original one");

  }

 //add validate transaction data function

}