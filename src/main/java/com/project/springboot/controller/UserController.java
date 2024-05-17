package com.project.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.springboot.common.Constants;
import com.project.springboot.common.Result;
import com.project.springboot.entity.UserDTO;
import com.project.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.lang.constant.Constable;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.project.springboot.service.IUserService;
import com.project.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.ApiListing;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

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
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userdto) {
        String username= userdto.getUsername();
        String password=userdto.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(userService.register(userdto));
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userdto) {
        String username= userdto.getUsername();
        String password=userdto.getPassword();
        userdto.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userdto);
        return Result.success(userdto);
    }

    //删除
    @DeleteMapping("/{id}")
    public Result datete(@PathVariable Integer id){
        return Result.success(userService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result batchDelete(@RequestBody List<Integer> id){
            return Result.success(userService.removeByIds(id));
            }

    //全局查询
    @GetMapping
    public Result findAll(){
        return Result.success(userService.list());
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(userService.getById(id));
    }
    @GetMapping("/username/{username}")
        public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        System.out.println("查询用户信息======================================="+userService.getOne(queryWrapper).getUsername()+userService.getOne(queryWrapper).getAvatarUrl())  ;
            return Result.success(userService.getOne(queryWrapper));
        }

    //分页查询
    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String phone,
                           @RequestParam(defaultValue = "") String address){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!username.equals(""),"username",username);
        queryWrapper.like(!phone.equals(""),"phone",phone);
        queryWrapper.like(!address.equals(""),"address",address);
        queryWrapper.orderByDesc("id");
        User currentuser=TokenUtils.getCurrentUser();
        return Result.success(userService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }





}

