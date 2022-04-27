package com.dgyu.pure_design.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgyu.pure_design.common.Result;
import com.dgyu.pure_design.common.exception.BusinessException;
import com.dgyu.pure_design.entity.Files;
import com.dgyu.pure_design.mapper.FileMapper;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/*import org.springframework.data.redis.core.StringRedisTemplate;
*/import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

	@Value("${files.upload.path}")
	private String fileUploadPath;

	@Value("${server.ip}")
	private String serverIp;

	@Resource
	private FileMapper fileMapper;

	@Autowired
	private FastFileStorageClient storageClient;

	@Value("${fdfs.path}")
	private String fdfsPath;

	/*
	 * @Autowired private StringRedisTemplate stringRedisTemplate;
	 */

	/**
	 * 文件上传接口
	 * 
	 * @param file 前端传递过来的文件
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/upload")
	public String upload(@RequestParam MultipartFile file) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String type = FileUtil.extName(originalFilename);
		long size = file.getSize();

		// 定义一个文件唯一的标识码
		String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

		// File uploadFile = new File(fileUploadPath + fileUUID);
		// 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
		// File parentFile = uploadFile.getParentFile();
		// if(!parentFile.exists()) {
		// parentFile.mkdirs();
		// }

		String url = null;
		// 获取文件的md5
		String md5 = SecureUtil.md5(file.getInputStream());
		// 从数据库查询是否存在相同的记录
		Files dbFiles = getFileByMd5(md5);
		if (dbFiles != null) {
			url = dbFiles.getUrl();
		} else {
			// 上传文件到磁盘
			// file.transferTo(uploadFile);
			// 数据库若不存在重复文件，则不删除刚才上传的文件
			// url = "http://" + serverIp + ":9091/file/" + fileUUID;

			String fileExtName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
			StorePath storePath = storageClient.uploadFile(file.getInputStream(), size, fileExtName, null);
			if (null != storePath) {
				url = storePath.getFullPath();
			}
		}

		// 存储数据库
		Files saveFile = new Files();
		saveFile.setName(originalFilename);
		saveFile.setType(type);
		saveFile.setSize(size / 1024); // 单位 kb
		saveFile.setUrl(url);
		saveFile.setMd5(md5);
		fileMapper.insert(saveFile);

		// 从redis取出数据，操作完，再设置（不用查询数据库）
//        String json = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
//        List<Files> files1 = JSONUtil.toBean(json, new TypeReference<List<Files>>() {
//        }, true);
//        files1.add(saveFile);
//        setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files1));

		// 从数据库查出数据
//        List<Files> files = fileMapper.selectList(null);
//        // 设置最新的缓存
//        setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files));

		// 最简单的方式：直接清空缓存
		// flushRedis(Constants.FILES_KEY);

		return url;
	}

	/**
	 * 文件下载接口 http://localhost:9090/file/{fileUUID}
	 * 
	 * @param fileUUID
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation(value = "下载文件", notes = "下载文件")
	@RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
	public void download(@PathVariable String fileId, HttpServletResponse response) throws IOException {
		log.info("文件下载开始，fileId:{}", fileId);
		// 根据文件的唯一标识码获取文件
		// File uploadFile = new File(fileUploadPath + fileUUID);
		Files file = this.fileMapper.selectById(fileId);
		if (null == file) {
			throw new BusinessException("文件不存在" + fileId);
		}
		// 待下载文件名
		//response.reset();//这句话会清空跨域配置
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("utf-8");
		String group = StringUtils.substringBefore(file.getUrl(), "/");
		String path = StringUtils.substringAfter(file.getUrl(), "/");
		InputStream ins = storageClient.downloadFile(group, path, new DownloadCallback<InputStream>() {
			@Override
			public InputStream recv(InputStream ins) throws IOException {
				// 将此ins返回给上面的ins
				return ins;
			}
		});
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		byte[] buff = new byte[1024];
		int length = 0;
		try {
			in = new BufferedInputStream(ins);
			out = new BufferedOutputStream(response.getOutputStream());
			while ((length = in.read(buff)) != -1) {
				out.write(buff, 0, length);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@RequestMapping(value = "/download2", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void download2(String fileId, HttpServletResponse response) {
		log.info("文件下载download2开始，fileId:{}", fileId);
		// 根据文件的唯一标识码获取文件
		// File uploadFile = new File(fileUploadPath + fileUUID);
		Files file = this.fileMapper.selectById(fileId);
		if (null == file) {
			throw new BusinessException("文件不存在" + fileId);
		}
		// 待下载文件名
		//response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("utf-8");
		String group = StringUtils.substringBefore(file.getUrl(), "/");
		String path = StringUtils.substringAfter(file.getUrl(), "/");
		InputStream ins = storageClient.downloadFile(group, path, new DownloadCallback<InputStream>() {
			@Override
			public InputStream recv(InputStream ins) throws IOException {
				// 将此ins返回给上面的ins
				return ins;
			}
		});

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		byte[] buff = new byte[1024];
		int length = 0;
		try {
			in = new BufferedInputStream(ins);
			out = new BufferedOutputStream(response.getOutputStream());
			while ((length = in.read(buff)) != -1) {
				out.write(buff, 0, length);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过文件的md5查询文件
	 * 
	 * @param md5
	 * @return
	 */
	private Files getFileByMd5(String md5) {
		// 查询文件的md5是否存在
		QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("md5", md5);
		List<Files> filesList = fileMapper.selectList(queryWrapper);
		return filesList.size() == 0 ? null : filesList.get(0);
	}

//    @CachePut(value = "files", key = "'frontAll'")
	@PostMapping("/update")
	public Result update(@RequestBody Files files) {
		fileMapper.updateById(files);
		// flushRedis(Constants.FILES_KEY);
		return Result.success();
	}

	@GetMapping("/detail/{id}")
	public Result getById(@PathVariable Integer id) {
		return Result.success(fileMapper.selectById(id));
	}

	// 清除一条缓存，key为要清空的数据
//    @CacheEvict(value="files",key="'frontAll'")
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Integer id) {
		Files files = fileMapper.selectById(id);
		files.setIsDelete(true);
		fileMapper.updateById(files);
		// flushRedis(Constants.FILES_KEY);
		return Result.success();
	}

	@PostMapping("/del/batch")
	public Result deleteBatch(@RequestBody List<Integer> ids) {
		// select * from sys_file where id in (id,id,id...)
		QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", ids);
		List<Files> files = fileMapper.selectList(queryWrapper);
		for (Files file : files) {
			file.setIsDelete(true);
			fileMapper.updateById(file);
		}
		return Result.success();
	}

	/**
	 * 分页查询接口
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@GetMapping("/page")
	public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
			@RequestParam(defaultValue = "") String name) {

		QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
		// 查询未删除的记录
		queryWrapper.eq("is_delete", false);
		queryWrapper.orderByDesc("id");
		if (!"".equals(name)) {
			queryWrapper.like("name", name);
		}
		Page<Files> page = fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
		page.getRecords().forEach(file -> {
			file.setUrl(fdfsPath + file.getUrl());
		});
		return Result.success(page);
	}

}
