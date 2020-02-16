package com.myself.registercenter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceCenter implements IServer {

    //返回处理器核的数量
    private static int availableProcessors = Runtime.getRuntime().availableProcessors();

    //初始化线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);

    //注册到pools的service
    private static  final HashMap<String, Class> serviceRegister = new HashMap<>();

    static {
        System.out.println("availableProcessors:" + availableProcessors);
    }

    private  static  boolean isRunning = false;

    private  static  int port;

    public ServiceCenter(int p){
        port = p;
    }

    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();;
    }

    @Override
    public void start() throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
        System.out.println("start server, port:" + port);
        try{
            while (true){
                // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
                Socket client = server.accept();
                System.out.println("accept socket client");
                executor.execute(new ServiceTask(client));
            }
        }finally {
            server.close();
        }
    }



    @Override
    public void register(Class serviceInterface, Class impl) {
        serviceRegister.put(serviceInterface.getName(), impl);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return port;
    }

    private  static  class ServiceTask implements  Runnable{

        Socket client = null;

        public  ServiceTask(Socket socket){
            this.client = socket;
        }

        @Override
        public void run() {


            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {
                // 2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
                input = new ObjectInputStream(client.getInputStream());

                String serviceName = input.readUTF();
                String methodName = input.readUTF();

                System.out.println("serviceName：" + serviceName  + "methodName:" + methodName);
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Class serviceClass = serviceRegister.get(serviceName);
                if (serviceClass == null) {
                    throw new ClassNotFoundException(serviceName + " not found");
                }
                Method method = serviceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(), arguments);


                // 3.将执行结果反序列化，通过socket发送给客户端
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }
}