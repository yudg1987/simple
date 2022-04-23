package com.dgyu.pure_design.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dgyu.pure_design.controller.dto.UserDTO;
import com.dgyu.pure_design.controller.dto.UserPasswordDTO;
import com.dgyu.pure_design.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgyu
 * @since 2022-01-26
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> objectPage, String username, String email, String address);
}
