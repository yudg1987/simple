package com.dgyu.pure_design.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgyu.pure_design.common.Result;
import com.dgyu.pure_design.entity.Book;
import com.dgyu.pure_design.mapper.BookDao;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "书籍管理相关接口", tags = "书籍管理相关接口类")
@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {

	@Resource
	private BookDao bookDao;

	@GetMapping("/getBooks/{page}/{size}")
	@ApiOperation("分页查询书籍")
	public Result getBooks(@PathVariable("page") int page, @PathVariable("size") int size) {
		log.debug("getBooks start ,page:{},size:{}", page, size);
		IPage<Book> bookPage = new Page<>(page, size);// 参数一是当前页，参数二是每页个数
		bookPage = bookDao.selectPage(bookPage, null);
		return Result.success(bookPage);
	}

	@ApiOperation("添加书籍")
	@PostMapping("/addBook")
	public Result addBook(@RequestBody Book book) {
		bookDao.insert(book);
		return Result.success();
	}

	@GetMapping("/getBookById/{id}")
	@ApiOperation("根据ID查询书籍")
	public Result getBookById(@PathVariable("id") int id) {
		Book book = bookDao.selectById(id);
		return Result.success(book);
	}

	@PostMapping("/updateBook")
	@ApiOperation("根据ID更新书籍")
	public Result updateBook(@RequestBody Book book) {
		bookDao.updateById(book);
		return Result.success();
	}

	@DeleteMapping("/delete/{id}")
	@ApiOperation("根据ID删除书籍")
	public Result updateBook(@PathVariable("id") Integer id) {
		bookDao.deleteById(id);
		return Result.success();
	}
}
