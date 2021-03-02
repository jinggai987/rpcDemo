package com.zy.rpcdemo.rpc;

import com.zy.rpcdemo.IUserService;

public class Client {

    public static void main(String[] args) {
        IUserService iUserService = (IUserService) Stub.getStub(IUserService.class);
        System.out.println(iUserService.findUserById(1).getName());

    }

}
