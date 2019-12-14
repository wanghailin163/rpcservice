package com.myself.provider;

import com.myself.registercenter.IServer;
import com.myself.registercenter.ServiceCenter;

import java.io.IOException;

/**
 * @Description TODO
 * @Author wanghailin
 * @Date 2019-12-14
 * @Version 1.0
 */
public class ServiceRegister {

    public static void main(String[] args) {

        try {
            IServer serviceServer = new ServiceCenter(8088);
            //注册接口服务
            serviceServer.register(IMessageService.class, MessageServiceImpl.class);
            //启动服务并进行监听客户端发送的报文
            serviceServer.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
