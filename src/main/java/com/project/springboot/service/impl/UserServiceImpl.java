package com.project.springboot.service.impl;

import com.project.springboot.entity.User;
import com.project.springboot.mapper.UserMapper;
import com.project.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
