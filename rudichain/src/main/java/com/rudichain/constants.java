package com.rudichain;

import com.rudichain.backend.block;

public interface constants{
    int INITIAL_DIFFICULTY = 4;
    int MINE_RATE = 20000;
    block GENISIS_BLOCK = new block("null","null",null,1,0,INITIAL_DIFFICULTY);
    double MINING_REWARD = 50;
    double STARTING_BALANCE = 100;
}