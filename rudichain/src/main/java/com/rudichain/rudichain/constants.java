package com.rudichain.rudichain;

public enum constants{
    INITIAL_DIFFICULTY(3), MINE_RATE(1000), DEFAULT_PORT(8080);
    private int p;
    constants(int p){
        this.p = p;
    }
    public int getValue(){
        return p;
    }
}