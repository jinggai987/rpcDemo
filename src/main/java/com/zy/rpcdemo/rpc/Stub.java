package com.zy.rpcdemo.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class Stub {

    static Object getStub(Class c) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("invoke方法");
                Socket socket = new Socket("127.0.0.1",8088);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                objectOutputStream.writeUTF(c.getName());
                objectOutputStream.writeUTF(method.getName());
                objectOutputStream.writeObject(method.getParameterTypes());
                objectOutputStream.writeObject(args);
                objectOutputStream.flush();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                return objectInputStream.readObject();
            }
        };

        Object o = Proxy.newProxyInstance(c.getClassLoader(),new Class[]{c}, invocationHandler);
        System.out.println("类名1："+o.getClass().getName());
        System.out.println("类名2："+o.getClass().getInterfaces()[0]);
        return o;
    }

}
