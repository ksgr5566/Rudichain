package com.rudichain.backend;

import com.rudichain.constants;
import com.rudichain.cryptography.Hash;

import java.sql.Timestamp;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class block{
     String lastHash, hash, data;
     long timestamp, nonce;
     int difficulty;

     block(String lastHash, String hash, String data, long timestamp, long nonce, int difficulty){
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = data;
        this.nonce = nonce;
        this.difficulty = difficulty;
     }

     static block genesis(){
         return new block("null","null","null",1,0,constants.INITIAL_DIFFICULTY.getValue());
     }

     static int adjustDifficulty(block Block, long timestamp){
         int difficulty = Block.difficulty;
         if(difficulty<1) return 1;
         if((timestamp - Block.timestamp) > constants.MINE_RATE.getValue()) return difficulty-1;
         return difficulty+1;
     }

     static block mineBlock(block prevBlock, String data){
         String lastHash = prevBlock.hash;
         int difficulty = prevBlock.difficulty;
         String hash;
         long timestamp;
         long nonce = 0;

         do{
             nonce++;
             timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
             difficulty = block.adjustDifficulty(prevBlock, timestamp);
             hash = Hash.SHA256(lastHash, data, timestamp, nonce, difficulty);
         } while(!(((new String(DatatypeConverter.parseHexBinary(hash), StandardCharsets.UTF_8)).substring(0, difficulty)).equals("0".repeat(difficulty))));

         return new block(lastHash, hash, data, timestamp, nonce, difficulty);

     }

}
