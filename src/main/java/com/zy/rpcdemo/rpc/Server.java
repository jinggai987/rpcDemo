package com.zy.rpcdemo.rpc;

import com.zy.rpcdemo.IUserService;
import com.zy.rpcdemo.IUserServiceImpl;

import javax.jws.Oneway;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static boolean running = true;

    private static HashMap<String, Class> registerTable = new HashMap<>();

    static {
        registerTable.put(IUserService.class.getName(), IUserServiceImpl.class);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ServerSocket socket = new ServerSocket(8088);
        while (running) {
            Socket client = socket.accept();
            process(client);
            client.close();
        }
        socket.close();
    }

    public static void process(Socket socket) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        String className = objectInputStream.readUTF();
        String methodName = objectInputStream.readUTF();
        Class[] parameterTypes = (Class[]) objectInputStream.readObject();
        Object[] args = (Object[]) objectInputStream.readObject();

        Object object = registerTable.get(className).newInstance();
        Method method = object.getClass().getMethod(methodName, parameterTypes);
        Object returnObject = method.invoke(object, args);

        objectOutputStream.writeObject(returnObject);
        objectOutputStream.flush();
        System.out.println("process方法");
    }
}
