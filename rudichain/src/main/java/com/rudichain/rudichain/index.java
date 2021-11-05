package com.rudichain.rudichain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class index {

    @RequestMapping("/api")
    @GetMapping()
    public String helloworld(){
        return "Hello Server!!!";
    }

    @RequestMapping("/api/block")
    @GetMapping()
    public String thankworld(){
        return "Hello block!!!";
    }
    
}
