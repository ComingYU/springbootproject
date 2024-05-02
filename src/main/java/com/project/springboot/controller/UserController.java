package com.project.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.project.springboot.service.IUserService;
import com.project.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ComingYU
 * @since 2024-05-02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    //新增或更新
    @PostMapping
    public boolean save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    //删除
    @DeleteMapping("/{id}")
    public boolean datete(@PathVariable Integer id){
        return userService.removeById(id);
    }

    //批量删除
    @PostMapping("/del/batch")
    public boolean batchDelete(@RequestBody List<Integer> id){
            return userService.removeByIds(id);
            }

    //全局查询
    @GetMapping
    public List<User> findAll(){
        return userService.list();
    }

    //根据id查询
    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id){
        return userService.getById(id);
    }

    //分页查询
    @GetMapping("/page")
    public Page<User> page(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!username.equals(""),"username",username);
        queryWrapper.like(!email.equals(""),"email",email);
        queryWrapper.like(!address.equals(""),"address",address);
        queryWrapper.orderByDesc("id");
        return userService.page(new Page<>(pageNum,pageSize),queryWrapper);
    }





}

