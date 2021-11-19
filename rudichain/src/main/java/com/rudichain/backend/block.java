package com.rudichain.backend;

import com.rudichain.constants;
import com.rudichain.cryptography.Hash;
import com.rudichain.wallet.Transaction;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class block implements constants{
     public String lastHash, hash;
     public long timestamp, nonce;
     public int difficulty;
     public ArrayList<Transaction> data;

     public block(String lastHash, String hash, ArrayList<Transaction> data, long timestamp, long nonce, int difficulty){
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = data;
        this.nonce = nonce;
        this.difficulty = difficulty;
     }

     static int adjustDifficulty(block Block, long timestamp){
         int difficulty = Block.difficulty;
         if(difficulty<5) return 4;
         if((timestamp - Block.timestamp) > MINE_RATE) return difficulty-1;
         return difficulty+1;
     }

     static block mineBlock(block prevBlock, ArrayList<Transaction> data){
         String lastHash = prevBlock.hash;
         int difficulty = prevBlock.difficulty;
         String hash;
         long timestamp;
         long nonce = 0;

         do{
             nonce++;
             System.out.println(nonce);
             timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
             difficulty = block.adjustDifficulty(prevBlock, timestamp);
             hash = Hash.SHA256(lastHash, data, timestamp, nonce, difficulty);
             System.out.println(hash);
             //System.out.println(new String(DatatypeConverter.parseHexBinary(hash), StandardCharsets.UTF_8));
         } //while(!(((new String(DatatypeConverter.parseHexBinary(hash), StandardCharsets.UTF_8)).substring(0, difficulty)).equals("0".repeat(difficulty))));
         while(!((hash.substring(0, difficulty)).equals("0".repeat(difficulty))));

         return new block(lastHash, hash, data, timestamp, nonce, difficulty);

     }

}
