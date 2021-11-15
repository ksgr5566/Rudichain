package com.rudichain.rudichain;

import com.rudichain.rudichain.backend.block;

public interface constants{
    int INITIAL_DIFFICULTY = 10;
    int MINE_RATE = 10000;
    int DEFAULT_PORT = 8080;
    block GENISIS_BLOCK = new block("null","null",null,1,0,INITIAL_DIFFICULTY);
    double MINING_REWARD = 50;
    double STARTING_BALANCE = 100;
}