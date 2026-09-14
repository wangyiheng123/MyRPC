package com.langchain.common.ratelimit.provider;

import com.langchain.common.ratelimit.RateLImit;
import com.langchain.common.ratelimit.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

public class RateLimitProvider {

    private Map<String, RateLImit> rateLImitMap = new HashMap<>();

    public RateLImit getRateLimit(String interfaceName){
        if (!rateLImitMap.containsKey(interfaceName)){
            RateLImit rateLImit = new TokenBucketRateLimitImpl(100,10);
            rateLImitMap.put(interfaceName,rateLImit);
            return rateLImit;
        }
        return rateLImitMap.get(interfaceName);
    }

}
