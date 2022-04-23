package com.dgyu.pure_design.controller.dto;

import lombok.Data;

import java.util.List;

import com.dgyu.pure_design.entity.Menu;


/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
