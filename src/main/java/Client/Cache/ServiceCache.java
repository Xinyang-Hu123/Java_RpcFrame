package Client.Cache;

import Client.loadBalance.LoadBalance;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//本地缓存
public class ServiceCache {
    private Map<String, List<Instance>> cache;
    private LoadBalance loadBalance;

    public ServiceCache(LoadBalance loadBalance) {
        this.cache = new HashMap<>();
        this.loadBalance = loadBalance;
    }
    //存入缓存
    public void addCache(String serviceName, List<Instance> instances) {
        cache.put(serviceName, instances);
    }
    //从缓存获取实例
    public Instance getCache(String serviceName) {
        List<Instance> instances = cache.get(serviceName);
        if (instances == null || instances.isEmpty()){
            return null;
        }
        return loadBalance.select(instances);
    }

    //删除缓存
    public void removeCache(String serviceName) {
        cache.remove(serviceName);
    }








}
