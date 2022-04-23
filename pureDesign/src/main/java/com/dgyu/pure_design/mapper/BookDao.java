package com.dgyu.pure_design.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgyu.pure_design.entity.Book;

import java.util.List;

public interface BookDao extends BaseMapper<Book> {
	List<Book> findBooks();

}
