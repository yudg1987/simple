package com.dgyu.pure_design.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgyu.pure_design.entity.Role;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgyu
 * @since 2022-02-10
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
