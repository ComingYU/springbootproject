package com.project.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.springboot.entity.User;
import com.project.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    public boolean saveUser(User user) {
//        if(user.getId()==null){
//            return save(user);
//        }else {
//            return updateById(user);
//        }
        return saveOrUpdate(user);
    }

}
