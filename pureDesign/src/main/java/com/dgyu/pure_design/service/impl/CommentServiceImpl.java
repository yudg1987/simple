package com.dgyu.pure_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgyu.pure_design.entity.Comment;
import com.dgyu.pure_design.mapper.CommentMapper;
import com.dgyu.pure_design.service.ICommentService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgyu
 * @since 2022-03-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentDetail(Integer articleId) {

        return commentMapper.findCommentDetail(articleId);
    }
}
