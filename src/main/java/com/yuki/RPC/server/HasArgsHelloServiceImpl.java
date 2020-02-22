package com.yuki.RPC.server;

import com.yuki.RPC.common.HasArgsHelloService;

/**
 * @Auther: yuki
 * @Date: 2020/2/22 17:22
 * @Description:
 */
public class HasArgsHelloServiceImpl implements HasArgsHelloService {
    @Override
    public String hello(String msg) {
        return msg;
    }
}
