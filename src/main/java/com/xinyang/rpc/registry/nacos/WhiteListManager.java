package com.xinyang.rpc.registry.nacos;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class WhiteListManager {
    private static final Set<String> ALLOWED_IPS = new ConcurrentSkipListSet<>();

    static {
        ALLOWED_IPS.add("127.0.0.1");
    }
    public static boolean isAllowedIp(String ip) {
        return ALLOWED_IPS.contains(ip);
    }

}
