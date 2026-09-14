package com.langchain.common.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

public class CircuitBreakProvider {

    private Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();

    public synchronized CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;

        if (!circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker = new CircuitBreaker(1,0.5,10000);
            circuitBreakerMap.put(serviceName,circuitBreaker);
        }else {
            circuitBreaker = circuitBreakerMap.get(serviceName);
        }
        return circuitBreaker;
    }

}
