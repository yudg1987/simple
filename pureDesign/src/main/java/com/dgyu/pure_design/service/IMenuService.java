package com.dgyu.pure_design.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dgyu.pure_design.entity.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgyu
 * @since 2022-02-10
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
