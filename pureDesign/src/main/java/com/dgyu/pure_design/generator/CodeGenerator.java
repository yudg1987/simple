package com.dgyu.pure_design.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @ClassName CodeGenerator
 * @Description 代码生成器
 * @Author dgyu
 * @Date 2020/8/1
 */
public class CodeGenerator
{
	// 代码生成到哪个包下面
	private static String packsge = "com.dgyu.pure_design";
	// 数据库登录名
	private static String dataBaseUsername = "root";
	// 数据库登录密码
	private static String dataBasePassword = "iflytek";
	// 数据库链接地址
	private static String dataBase = "jdbc:mysql://172.31.160.210:3306/wssc_simple?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useCursorFetch=true&defaultFetchSize=1000&serverTimezone=GMT%2B8&useSSL=false";
	// 使用自定义模板生成实体类
	private static String customEntity = "templates/entity.java";
	// 生成的代码输出得路径
	private static String outPath = System.getProperty("user.dir");

	/**
	 * <p>
	 * 读取控制台内容
	 * </p>
	 */
	public static String scanner(String tip)
	{
		Scanner scanner = new Scanner(System.in);
		StringBuilder help = new StringBuilder();
		help.append("请输入" + tip + "：");
		System.out.println(help.toString());
		if (scanner.hasNext())
		{
			String ipt = scanner.next();
			if (StrUtil.isNotEmpty(ipt))
			{
				return ipt;
			}
		}
		throw new MybatisPlusException("请输入正确的" + tip + "！");
	}


	public static void main(String[] args)
	{
		AutoGenerator generator = new AutoGenerator();
		// 全局变量配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = outPath;// 当前项目
		gc.setOutputDir(projectPath + "/src/main/java"); // 输出路径
		gc.setFileOverride(true); // 默认 false ,是否覆盖已生成文件
		gc.setOpen(false); // 默认true ,是否打开输出目录
		gc.setEnableCache(false); // 默认false,是否开启二级缓存
		gc.setAuthor("dgyu"); // 作者
		gc.setSwagger2(true); // 默认false
		gc.setBaseResultMap(true); // 默认false
		gc.setDateType(DateType.TIME_PACK); // 时间策略 默认TIME_PACK
		gc.setBaseColumnList(true); // 默认false 和basemodel相似
		gc.setEntityName("%sEntity");
		gc.setControllerName("%sController");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		gc.setIdType(IdType.ASSIGN_ID); // 指定生成的主键类型
		generator.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dc = new DataSourceConfig();
		dc.setDbQuery(new MySqlQuery()); // 数据库信息查询 //默认mysql
		dc.setDbType(DbType.MYSQL);// 数据库类型
		dc.setTypeConvert(new MySqlTypeConvert()); // 类型转换 默认mysql
		dc.setUrl(dataBase);
		dc.setDriverName("com.mysql.jdbc.Driver");
		dc.setUsername(dataBaseUsername);
		dc.setPassword(dataBasePassword);
		dc.setTypeConvert(new MySqlTypeConvert()
		{

			@Override
			public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType)
			{
				System.out.println("转换类型：" + fieldType);
				// 将数据库中blob转换成byte[]
				if (fieldType.toLowerCase().contains("blob"))
				{
					return DbColumnType.BYTE;
				}
				// tinyint转换成INTEGER
				if (fieldType.toLowerCase().contains("tinyint"))
				{
					return DbColumnType.INTEGER;
				}
				// 将数据库中datetime转换成Timestamp
				if (fieldType.toLowerCase().contains("datetime"))
				{
					return DbColumnType.TIMESTAMP;
				}
				// 将数据库中timestamp转换成Timestamp
				if (fieldType.toLowerCase().contains("timestamp"))
				{
					return DbColumnType.DATE;
				}
				return super.processTypeConvert(globalConfig, fieldType);
			}
		});
		generator.setDataSource(dc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(packsge);// 代码生成到哪个包下面
		// pc.setModuleName(""); //此处是所属模块名称
		// pc.setEntity("entity"); //默认entity,com.dazj.dgyu.controller,service,service.impl,mapper,mapper.xml
		generator.setPackageInfo(pc);
		// 自定义配置
		InjectionConfig cfg = new InjectionConfig()
		{
			@Override
			public void initMap()
			{
				// to do nothing
			}
		};
		/**
		 * 将xml生成到resource下面
		 */
		String templatePath = "/templates/mapper.xml.vm"; // Velocity模板
		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath)
		{
			@Override
			public String outputFile(TableInfo tableInfo)
			{
				// 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
				return projectPath + "/src/main/resources/mapper/"
				// + pc.getModuleName()
					+ "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
			}
		});
		cfg.setFileOutConfigList(focList);
		generator.setCfg(cfg);

		// 配置模板
		TemplateConfig tc = new TemplateConfig();
		// templates/entity.java 模板路径配置，默认在templates目录下，.vm 后缀不用加
		tc.setEntity(customEntity);// 使用自定义模板生成实体类
		tc.setXml("");
		generator.setTemplate(tc);

		// 数据库表配置
		StrategyConfig sc = new StrategyConfig();
		sc.setCapitalMode(false); // 是否大写命名 默认false
		sc.setSkipView(true); // 是否跳过试图 默认false
		sc.setNaming(NamingStrategy.underline_to_camel);// 表映射 驼峰命名
		sc.setColumnNaming(NamingStrategy.underline_to_camel); // 字段映射 驼峰
		sc.setEntityLombokModel(true); // 是否使用lombak 默认为false
		sc.setRestControllerStyle(true); // 默认false
		sc.setEntitySerialVersionUID(true); // 默认true
		sc.setEntityColumnConstant(true); // 默认false 将mysql字段名生成静态变量
		sc.setInclude(scanner("表名，多个英文逗号分割").split(",")); // 表名，用，隔开 需要生产
		// sc.setExclude(""); // 不需要生成 二选一
		sc.setEntityTableFieldAnnotationEnable(true); // 默认false 注释
		sc.setControllerMappingHyphenStyle(false); // 默认false
		sc.setLogicDeleteFieldName("is_delete"); // 逻辑删除字段名称
		generator.setStrategy(sc);

		// 模板引擎
		generator.setTemplateEngine(new VelocityTemplateEngine());
		generator.execute();
	}
}
