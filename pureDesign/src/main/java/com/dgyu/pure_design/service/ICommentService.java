package com.dgyu.pure_design.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dgyu.pure_design.entity.Comment;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgyu
 * @since 2022-03-22
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer articleId);
}
