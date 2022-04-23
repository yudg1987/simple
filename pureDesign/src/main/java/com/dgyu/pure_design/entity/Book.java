package com.dgyu.pure_design.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@TableName("book")
@Data
public class Book {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String name;
	private String author;
}
