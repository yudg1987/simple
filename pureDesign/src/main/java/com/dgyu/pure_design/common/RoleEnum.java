package com.dgyu.pure_design.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum RoleEnum {

	ROLE_SUPER_ADMIN("超级管理员"), ROLE_ADMIN("管理员"), ROLE_STUDENT("学生"),ROLE_TEACHER("教师");

	RoleEnum(String desc) {
		this.desc = desc;
	}

	// 成员变量
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	// 普通方法
	public static String getName(String name) {
		for (RoleEnum c : RoleEnum.values()) {
			log.debug("c.name:{},name:{}", c.name(), name);
			if (c.name().equalsIgnoreCase(name)) {
				return c.getDesc();
			}
		}
		return null;
	}

}
