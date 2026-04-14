package Client.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {

    @Override
    public Instance select(List<Instance> instances) {
        Random random = new Random();
        int n = instances.size();
        int index = random.nextInt(n);
        return instances.get(index);
    }
}
