package com.dgyu.pure_design.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dgyu.pure_design.entity.Course;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgyu
 * @since 2022-03-03
 */
public interface ICourseService extends IService<Course> {

    Page<Course> findPage(Page<Course> page, String name);

    void setStudentCourse(Integer courseId, Integer studentId);
}
