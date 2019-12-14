package com.myself.provider;

/**
 * @Description TODO
 * @Author wanghailin
 * @Date 2019-12-14
 * @Version 1.0
 */
public class MessageServiceImpl implements IMessageService{

    @Override
    public String sendMessage(String message) {
        System.out.println("服务端收到消息 ===>message: " + message);
        return "success";
    }
}
