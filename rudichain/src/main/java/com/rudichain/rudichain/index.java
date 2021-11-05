package com.rudichain.rudichain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson; 

import com.rudichain.rudichain.backend.blockchain;
import com.rudichain.rudichain.network.pubsub;

@RestController

public class index{

    blockchain chain = new blockchain();
    pubsub client = new pubsub(chain);

    @RequestMapping("/api/blocks")
    @GetMapping()
    public String RespondWithChain(){
        Gson gson = new Gson();
        return gson.toJson(chain.chain);
    }

    @RequestMapping("/api/mine")
    @PostMapping()
    public ModelAndView PostData(@RequestBody String data){
        chain.addBlock(data);
        client.broadcastChain();
        System.out.println("Redirecting to /api/blocks....");
        return new ModelAndView("redirect:/api/blocks");
    }

    //to be thorouly tested

    
}
