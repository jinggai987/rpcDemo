package com.zy.rpcdemo;

public class IUserServiceImpl implements IUserService {
    @Override
    public User findUserById(int id) {
        System.out.println("findUserById方法");
        return new User(id,"Alice");
    }//直接new模拟数据库查询
}
