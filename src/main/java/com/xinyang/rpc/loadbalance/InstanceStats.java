package com.xinyang.rpc.loadbalance;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//实例状态 用于动态负载均衡

//公式 S(t) = ALPHA * Y(t) + (1 - ALPHA) * S(t-1)
//S(t)：当前时刻的“平滑负载”（用于决策）
//Y(t)：当前真实负载（比如CPU、QPS等）
//S(t-1)：上一时刻的平滑负载（历史状态）
//α（alpha）：权重（0~1之间）

public class InstanceStats {
    private final AtomicLong emaResponseTime = new AtomicLong(0);
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final double ALPHA = 0.3; //负载均衡系数，暂时先是他吧
    //todo 动态负载均衡 gemini给的代码
    public void updateResponseTime(long rtt) {
        long currentEma = emaResponseTime.get();
        if(currentEma == 0){
            emaResponseTime.set(rtt);
        } else{
            long nextEma = (long) (ALPHA * rtt + (1 - ALPHA) * currentEma);
            emaResponseTime.set(nextEma);
        }
    }

}
