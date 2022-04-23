package com.dgyu.pure_design.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dgyu.pure_design.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgyu
 * @since 2022-02-10
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
