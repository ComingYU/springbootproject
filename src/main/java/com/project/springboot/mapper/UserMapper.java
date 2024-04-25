package com.project.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.springboot.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.lang.constant.ConstantDesc;
import java.util.List;
import java.util.stream.BaseStream;

public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM `sys_user`")
    List<User> findAll();

    @Insert("INSERT into sys_user(username,password,email,phone,address) VALUES (#{username},#{password},#{email},#{phone},#{address})")
    int insert(User user);

    int update(User user);

    @Delete("delete from sys_user where id=#{id}")
    void deleteById(@Param("id") Integer id);

    @Select("select * from sys_user where username like concat('%', #{username}, '%') limit #{pageNum}, #{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize,String username);

    @Select("select count(*) from sys_user where username like concat('%',#{username},'%')")
    Integer selectTotal(String username);
}
