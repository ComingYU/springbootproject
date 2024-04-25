package com.project.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.springboot.entity.User;
import com.project.springboot.mapper.UserMapper;
import com.project.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询所有数据
    @GetMapping
    public List<User> findAll(){
        return userService.list();
    }
    //更改数据
    @PostMapping
    public boolean save(@RequestBody User user){
        return userService.saveUser(user);
    }
    //删除数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }

    //批量删除
    @PostMapping("/del/batch")
    public boolean batchDelete(@RequestBody List<Integer> id){
        return userService.removeByIds(id);
    }

    //mybatis-plus实现分页查询接口
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String username,
                                       @RequestParam(defaultValue = "") String email,
                                       @RequestParam(defaultValue = "") String address){
        IPage<User> page=new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(!username.equals(""),"username",username);
        queryWrapper.like(!email.equals(""),"email",email);
        queryWrapper.like(!address.equals(""),"address",address);
        queryWrapper.orderByDesc("id");
        return userService.page(page, queryWrapper);
    }
}
