package com.langchain.common.ratelimit;

public class TokenBucketRateLimitImpl implements RateLImit{

    private static int CAPACITY;
    private volatile int curCapcity;
    private volatile long timeStamp = System.currentTimeMillis();
    private static int RATE;

    public TokenBucketRateLimitImpl(int rate, int capacity){
        RATE = rate;
        CAPACITY = capacity;
        curCapcity = capacity;
    }

    @Override
    public synchronized boolean getToken() {
        if (curCapcity > 0){
            curCapcity--;
            return true;
        }

        long current = System.currentTimeMillis();

        if (current - timeStamp >= RATE){
            if ((current - timeStamp) / RATE >= 2){
                curCapcity += (int) ((current - timeStamp) / RATE) - 1;
            }

            if (curCapcity > CAPACITY){
                curCapcity = CAPACITY;
            }

            timeStamp = current;
            return true;
        }

        return false;
    }
}
