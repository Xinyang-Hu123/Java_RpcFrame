package com.xinyang.rpc.registry.nacos;

import com.xinyang.rpc.client.Cache.ServiceCache;
import com.xinyang.rpc.loadbalance.RandomLoadBalance;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

@Slf4j
public class NacosServiceRegistry {

    private final NamingService namingService;
    private ServiceCache serviceCache;
    public NacosServiceRegistry() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.put("username", "nacos");
        properties.put("password", "tlThF01zY1BO");
        this.namingService = NacosFactory.createNamingService(properties);
        this.serviceCache = new ServiceCache(new RandomLoadBalance());
    }

    //注册服务
    //todo 改这里，白名单
    public void register(String serviceName, String ip, int port) throws NacosException {
        if(!WhiteListManager.isAllowedIp(ip)){
            log.warn("非法注册请求：IP {} 不在白名单内，拒绝服务 [{}] 的注册", ip, serviceName);
            throw new RuntimeException("Security: IP not in whitelist");
        }
        Instance instance = new Instance();
        instance.setIp(ip);
        instance.setPort(port);
        instance.setServiceName(serviceName);


        namingService.registerInstance(serviceName, instance);
        log.info("服务 {} 注册成功，地址 {}:{}", serviceName, ip, port);
    }



    //发现服务
    public Instance discovery(String serviceName) throws NacosException {
        //判断当前实例是否存在
        Instance instance = serviceCache.getCache(serviceName);
        if (instance != null) {
            return instance;
        }

        //从nacos获取实例
        List<Instance> instances = namingService.getAllInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            return null;
        }

        //存进缓存
        serviceCache.addCache(serviceName, instances);

        namingService.subscribe(serviceName, event -> {
            NamingEvent namingEvent = (NamingEvent) event;
            serviceCache.addCache(serviceName, namingEvent.getInstances());
        });

        return instances.get(0);
    }










}
