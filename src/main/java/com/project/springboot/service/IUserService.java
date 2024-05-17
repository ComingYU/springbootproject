package com.project.springboot.service;

import com.project.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.springboot.entity.UserDTO;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ComingYU
 * @since 2024-05-02
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userdto);

    User register(UserDTO userdto);
}
