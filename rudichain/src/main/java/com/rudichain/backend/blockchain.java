package com.rudichain.backend;

//how to get dependencies?

public class blockchain {
    
//this.chain

void addBlock({data}){

}

void replaceChain(chain, validateTransactions, onSuccess) {
    if (chain.length <= this.chain.length) { 
        //this is referring to which object?
      System.out.println("The incoming chain must be longer");
      return;
    }
    if (!Blockchain.isValidChain(chain)) {
        System.out.println("The incoming chain must be valid");
        return;
      }

      //isvalidchain func.?
      if (validateTransactions && !this.validTransactionData({ chain })) { 
    System.out.println("The incoming chain has invalid data");
        return;
      } 
      if (onSuccess) onSuccess();

      System.out.println("replacing chain with" + chain);
      this.chain = chain;//doubt.
    }

   boolean validTransactionData({ chain }) {//passing chain? //boolean type
        for (int i=1; i<chain.length; i++) {
          const block = chain[i]; //static final instead of const?
          const transactionSet = new Set();
          int rewardTransactionCount = 0;  
    
          for (let transaction of block.data) {// ?
            if (transaction.input.address == REWARD_INPUT.address) {
              rewardTransactionCount += 1;
    
              if (rewardTransactionCount > 1) {
                System.out.println("Miner rewards exceed limit");
                return false;
              }
    
              if (Object.values(transaction.outputMap)[0] != MINING_REWARD) {
                System.out.println("Miner reward amount is invalid");
                return false;
              }
            } else {
              if (!Transaction.validTransaction(transaction)) {
                System.out.println("Invalid transaction");
                return false;
              }
    
              const trueBalance = Wallet.calculateBalance({ //static final?
                chain: this.chain,
                address: transaction.input.address
              });
    
              if (transaction.input.amount != trueBalance) {
                System.out.println("Invalid input amount");
                return false;
              }
    
              if (transactionSet.has(transaction)) {
                System.out.println("An identical transaction appears more than once in the block");
                return false;
              } else {
                transactionSet.add(transaction);
              }
            }
          }
        }
    
        return true;
      }

      static isValidChain(chain) {
        if (JSON.stringify(chain[0]) != JSON.stringify(Block.genesis())) {
          return false;
        }
    
        for (int i=1; i<chain.length; i++) {//
          const { timestamp, lastHash, hash, nonce, difficulty, data } = chain[i];
          const actualLastHash = chain[i-1].hash;
          const lastDifficulty = chain[i-1].difficulty;
          //setting variables..
    
          if (lastHash != actualLastHash) return false;
    
          const validatedHash = cryptoHash(timestamp, lastHash, data, nonce, difficulty);
    
          if (hash != validatedHash) return false;
    
          if (Math.abs(lastDifficulty - difficulty) > 1) return false;
        }
    
        return true;
      }

    }

    //is there any export statement in java for exporting this blockchain class?