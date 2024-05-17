package com.project.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.springboot.Exception.ServiceException;
import com.project.springboot.common.Constants;
import com.project.springboot.entity.User;
import com.project.springboot.entity.UserDTO;
import com.project.springboot.mapper.UserMapper;
import com.project.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ComingYU
 * @since 2024-05-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log log= Log.get();
    @Override
    public UserDTO login(UserDTO userdto) {
        User one=getUserInfo(userdto);
        if(one!=null){
            BeanUtil.copyProperties(one,userdto,true);
            String token=TokenUtils.generateToken(one.getId().toString(),one.getPassword());
            userdto.setToken(token);
            return userdto;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }

    @Override
    public User register(UserDTO userdto) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userdto.getUsername());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            log.error("重复的用户名",e);
            throw new ServiceException(Constants.CODE_500,"用户名已存在");
        }
        if(one==null){
            one=new User();
            BeanUtil.copyProperties(userdto,one,true);
            save(one);
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名已存在");
        }
        return one;
    }
    private User getUserInfo(UserDTO userdto){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userdto.getUsername());
        queryWrapper.eq("password",userdto.getPassword());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            log.error("重复的用户名和密码",e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }
}
