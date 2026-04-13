package Nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.Properties;

public class NacosServiceRegistry {

    private final NamingService namingService;

    public NacosServiceRegistry() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.put("username", "nacos");
        properties.put("password", "tlThF01zY1BO");
        this.namingService = NacosFactory.createNamingService(properties);
    }

    //注册服务
    public void register(String serviceName, String ip, int port) throws NacosException {
        namingService.registerInstance(serviceName, ip, port);
    }

    //发现服务
    public Instance discovery(String serviceName) throws NacosException {
        return namingService.selectOneHealthyInstance(serviceName);
    }







}
