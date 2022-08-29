package com.imooc.miaosha.service;


import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 创建bean实例
public class UserService {

    @Autowired  // 根据属性类型自动装配
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

    @Transactional // 事务
    public boolean tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("2222");
        userDao.insert(u1);

        User u2= new User();
        u2.setId(1);
        u2.setName("11111");
        userDao.insert(u2);

        return true;
    }

}
