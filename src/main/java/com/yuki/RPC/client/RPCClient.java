package com.yuki.RPC.client;

import com.yuki.RPC.client_stub.ServiceProxy;
import com.yuki.RPC.common.HasArgsHelloService;
import com.yuki.RPC.common.NoArgsHelloService;

/**
 * @Auther: yuki
 * @Date: 2020/2/22 17:11
 * @Description:
 */
public class RPCClient {


    public static void main(String[] args){
        NoArgsHelloService noArgsHelloService = (NoArgsHelloService) ServiceProxy.create(NoArgsHelloService.class);
        System.out.println(noArgsHelloService.hello());

        HasArgsHelloService hasArgsHelloService = (HasArgsHelloService) ServiceProxy.create(HasArgsHelloService.class);
        System.out.println(hasArgsHelloService.hello("hello netty rpc"));
    }
}
