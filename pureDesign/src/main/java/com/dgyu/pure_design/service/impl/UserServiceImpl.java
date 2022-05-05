package com.dgyu.pure_design.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgyu.pure_design.common.Constants;
import com.dgyu.pure_design.common.RoleEnum;
import com.dgyu.pure_design.common.exception.BusinessException;
import com.dgyu.pure_design.controller.dto.UserDTO;
import com.dgyu.pure_design.controller.dto.UserPasswordDTO;
import com.dgyu.pure_design.entity.Menu;
import com.dgyu.pure_design.entity.User;
import com.dgyu.pure_design.mapper.RoleMapper;
import com.dgyu.pure_design.mapper.RoleMenuMapper;
import com.dgyu.pure_design.mapper.UserMapper;
import com.dgyu.pure_design.service.IMenuService;
import com.dgyu.pure_design.service.IUserService;
import com.dgyu.pure_design.util.TokenUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dgyu
 * @since 2022-01-26
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	private static final Log LOG = Log.get();

	@Resource
	private UserMapper userMapper;

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private RoleMenuMapper roleMenuMapper;

	@Resource
	private IMenuService menuService;

	@Override
	public UserDTO login(UserDTO userDTO) {
		User one = getUserInfo(userDTO);
		if (one != null) {
			BeanUtil.copyProperties(one, userDTO, true);
			// 设置token
			String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
			userDTO.setToken(token);

			String role = one.getRole(); // ROLE_ADMIN
			// 设置用户的菜单列表
			List<Menu> roleMenus = getRoleMenus(role);
			userDTO.setMenus(roleMenus);
			return userDTO;
		} else {
			throw new BusinessException(Constants.CODE_600, "用户名或密码错误");
		}
	}

	@Override
	public User register(UserDTO userDTO) {
		User one = getUserInfo(userDTO);
		if (one == null) {
			one = new User();
			BeanUtil.copyProperties(userDTO, one, true);
			// 默认一个普通用户的角色
			one.setRole(RoleEnum.ROLE_STUDENT.toString());
			save(one); // 把 copy完之后的用户对象存储到数据库
		} else {
			throw new BusinessException(Constants.CODE_600, "用户已存在");
		}
		return one;
	}

	@Override
	public void updatePassword(UserPasswordDTO userPasswordDTO) {
		int update = userMapper.updatePassword(userPasswordDTO);
		if (update < 1) {
			throw new BusinessException(Constants.CODE_600, "密码错误");
		}
	}

	@Override
	public Page<User> findPage(Page<User> page, String username, String email, String address) {
		Page<User> pageUser= userMapper.findPage(page, username, email, address);
		/*
		 * pageUser.getRecords().forEach(user->{
		 * user.setRole(RoleEnum.getName(user.getRole())); });
		 */
		return pageUser;
	}

	private User getUserInfo(UserDTO userDTO) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", userDTO.getUsername());
		queryWrapper.eq("password", userDTO.getPassword());
		User one;
		try {
			one = getOne(queryWrapper); // 从数据库查询用户信息
		} catch (Exception e) {
			LOG.error(e);
			throw new BusinessException(Constants.CODE_500, "系统错误");
		}
		return one;
	}

	/**
	 * 获取当前角色的菜单列表
	 * 
	 * @param roleFlag
	 * @return
	 */
	private List<Menu> getRoleMenus(String roleFlag) {
		Integer roleId = roleMapper.selectByFlag(roleFlag);
		// 当前角色的所有菜单id集合
		List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

		// 查出系统所有的菜单(树形)
		List<Menu> menus = menuService.findMenus("");
		// new一个最后筛选完成之后的list
		List<Menu> roleMenus = new ArrayList<>();
		// 筛选当前用户角色的菜单
		for (Menu menu : menus) {
			if (menuIds.contains(menu.getId())) {
				roleMenus.add(menu);
			}
			List<Menu> children = menu.getChildren();
			// removeIf() 移除 children 里面不在 menuIds集合中的 元素
			children.removeIf(child -> !menuIds.contains(child.getId()));
		}
		return roleMenus;
	}

}
