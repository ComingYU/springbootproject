package com.project.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName(value="sys_user")
public class User {
    private Integer id;
    private String username;
    @JsonIgnore//忽略属性，避免输出敏感信息
    private String password;
    private String nickname;
    private String email;
    private String address;
    private String phone;
}
