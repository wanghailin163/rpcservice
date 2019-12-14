package com.myself.registercenter;

import java.io.IOException;

/**
 * @Description 服务中心
 * @Author wanghailin
 * @Date 2019-12-14
 * @Version 1.0
 */
public interface IServer {

    /**
     * 停止服务
     */
    public void stop();

    /**
     * 启动服务
     * @throws IOException
     */
    public void start() throws IOException;

    /**
     * 注册某个服务
     * @param serviceInterface
     * @param impl
     */
    public void register(Class serviceInterface,Class impl);

    /**
     * 判断服务是否在运行
     * @return
     */
    public boolean isRunning();

    /**
     * 获取端口
     * @return
     */
    public int getPort();
}
