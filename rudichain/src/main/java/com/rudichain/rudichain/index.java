package com.rudichain.rudichain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.TimeUnit;

import com.google.gson.Gson; 

import com.rudichain.rudichain.backend.blockchain;
import com.rudichain.rudichain.network.pubsub;
import com.rudichain.rudichain.indexHelper.*;
import com.rudichain.rudichain.wallet.*;

@RestController

public class index{

    blockchain chain = new blockchain();
    Wallet wallet = createWallet();
    TransactionPool pool = new TransactionPool();
    pubsub client = new pubsub(chain, pool, wallet);
    TransactionMiner miner = new TransactionMiner(chain, client, pool, wallet);
    public static boolean synced = false;
    public static boolean synced2 = false;

    Gson gson = new Gson();

    Wallet createWallet(){
        Wallet tempWallet = null;
        try{
            tempWallet = new Wallet();
        }
        catch(Exception e){
            System.out.println("Exception caught while creating wallet in index");
        }
        return tempWallet;
    }

    @RequestMapping("/api/blocks")
    @GetMapping()
    public String RespondWithChain() throws InterruptedException{ 
        if(!synced2){
            client.introduceSelf2();
            TimeUnit.SECONDS.sleep(4);
            synced2 = true;
        } 
        return gson.toJson(chain.chain);
    }

    // @RequestMapping("/api/mine")
    // @PostMapping()
    // public ModelAndView PostData(@RequestBody String data) throws InterruptedException{
    //     if(!synced2){
    //         client.introduceSelf2();
    //         TimeUnit.SECONDS.sleep(4);
    //         synced2 = true;
    //     }
    //     chain.addBlock(data);
    //     client.broadcastChain();
    //     System.out.println("Redirecting to /api/blocks....");
    //     return new ModelAndView("redirect:/api/blocks");
    // }

    @RequestMapping("api/transact")
    @PostMapping()
    public ResponseEntity PostTransaction(@RequestBody postTransaction transactionData) throws InterruptedException{

        if(!synced){
           client.introduceSelf();
           TimeUnit.SECONDS.sleep(4);
           synced = true;
        }
        
        double amount = transactionData.getAmount();
        String recipient = transactionData.getRecipient();

        Transaction transaction = pool.exists(wallet.publicKey);

        try{
            if(transaction!=null){
                transaction.update(wallet,recipient, amount);
            }else{
                transaction = wallet.createTransaction(recipient, amount, this.chain);
            }
        }catch(InvalidTransaction e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Transaction!! Please try again!");
        }
        pool.setTransaction(transaction);

        client.broadcastTransactions(transaction);
        
        return ResponseEntity.ok(gson.toJson(transaction));
  
    }

    @RequestMapping("api/transactions")
    @GetMapping()
    public String RespondWithTransactionPool() throws InterruptedException{
        if(!synced){
            client.introduceSelf();
            TimeUnit.SECONDS.sleep(4);
            synced = true;
        }
        return gson.toJson(pool.transactionMap);
    }

    @RequestMapping("/api/mine-transactions")
    @GetMapping()
    public ModelAndView mine(){ 
        miner.mineTransactions();
        System.out.println("Mining in process");
        System.out.println("Redirecting to /api/blocks....");
        return new ModelAndView("redirect:/api/blocks");
    }

    @RequestMapping("/api/my-wallet")
    @GetMapping()
    public String myWallet(){
        String address = this.wallet.publicKey; 
        return new Gson().toJson(new walletInfo(address, Wallet.calcBalance(chain, address)));
    }
    
}