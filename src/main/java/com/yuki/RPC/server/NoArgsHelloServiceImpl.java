package com.yuki.RPC.server;

import com.yuki.RPC.common.NoArgsHelloService;

/**
 * @Auther: yuki
 * @Date: 2020/2/22 17:21
 * @Description:
 */
public class NoArgsHelloServiceImpl implements NoArgsHelloService {
    @Override
    public String hello() {
        return "hello";
    }
}
