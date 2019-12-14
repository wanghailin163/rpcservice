package com.myself.provider;

/**
 * @Description 服务提供发送消息的接口
 * @Author wanghailin
 * @Date 2019-12-14
 * @Version 1.0
 */
public interface IMessageService {

    /**
     * 发送消息
     * @param message
     * @return
     */
    String sendMessage(String message);
}
