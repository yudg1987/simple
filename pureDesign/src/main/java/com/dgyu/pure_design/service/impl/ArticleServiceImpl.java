package com.dgyu.pure_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgyu.pure_design.entity.Article;
import com.dgyu.pure_design.mapper.ArticleMapper;
import com.dgyu.pure_design.service.IArticleService;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgyu
 * @since 2022-03-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
