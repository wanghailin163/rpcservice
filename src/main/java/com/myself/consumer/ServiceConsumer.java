package com.myself.consumer;

import com.myself.provider.IMessageService;

import java.net.InetSocketAddress;

/**
 * @Description TODO
 * @Author wanghailin
 * @Date 2019-12-14
 * @Version 1.0
 */
public class ServiceConsumer {

    public static void main(String[] args) {
        InetSocketAddress inetSocketAddress =  new InetSocketAddress("localhost",8088);
        IMessageService service = RPCClient.getRemoteProxyObj(IMessageService.class, inetSocketAddress);
        System.out.println((service.sendMessage("你发送的报文内容")));
    }

}
