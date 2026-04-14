package Nacos;

import Client.Cache.ServiceCache;
import Client.loadBalance.RandomLoadBalance;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Properties;

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
    public void register(String serviceName, String ip, int port) throws NacosException {
        namingService.registerInstance(serviceName, ip, port);
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
