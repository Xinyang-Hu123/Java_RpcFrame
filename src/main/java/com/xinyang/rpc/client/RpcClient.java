package com.xinyang.rpc.client;

import com.xinyang.rpc.common.message.RpcRequest;
import com.xinyang.rpc.common.message.RpcResponse;

public interface RpcClient {

    RpcResponse sendRequest(RpcRequest request);


}
