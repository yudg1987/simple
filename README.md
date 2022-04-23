## 模块工程

包含以下特性

### spring-boot
工程基于 spring-boot 2.1.11.RELEASE 版本构建, 配置文件使用 yml 而不是 properties 格式文件, 这样更清晰, 而且支持中文. 另外通过CorsConfiguration类实现后端服务支持跨域调用

### mybatis-plus
ORM 框架使用 mybatis-plus 3.3.0 版本, 该框架基于 mybatis 开发, 自带分页插件, 而且我们自己的 Dao, Service 层接口可以继承其提供的基类, 从而节省很多基础方法的编写, 例: CardMapper, CardServiceImpl.
默认数据库为 mysql, 但也支持达梦: 修改 jdbc 相关配置, 配置 application.yml 中的 table-prefix 为数据库名加点(如: CARD_ENGINE.), 对象的 id 字段注解改为IdType.INPUT, 然后使用 IdWorderUitl 生成 id 并赋值给待插入对象, 最后重新打包项目

### hibernate-validation
通过spring-boot-starter-validation依赖实现参数自动校验
1. bean 上加注解(@NotNull, @PositiveOrZero等)
2. controller 层接口参数前加 @Validated 注解
3. 可以配置上面两步中的注解, 从而实现同一个bean 支持不同的验证规则(如: 更新时必须传 id, 保存时则不需要)

例: CardVO CardController

**注意: 目前只支持 Controller 层接口入参验证**

### lombok
增加 Lombok 依赖以后, 在 bean 上加 @Data 注解, 从而节省 get/set 方法的编写

**eclipse/IDEA 最好安装对应的 Lombok 插件, 否则在调用 get/set 方法时编辑器会提示编译错误. 但即使不装插件也不影响应用打包运行**

### MapStruct
增加 MapStruct 依赖, 在代码里定义 convertor 接口类后, 能够快速实现对象之间的转换编码. 比如要将 User 对象转成 UserVO 对象, 之前要么手动按个字段复制(userVO.setXX(user.getXX())), 要么使用类型 bean 拷贝的方式实现. 使用 MapStruct 后只需要定义转换接口, MapStruct自己会在项目编译时自动创建接口实现类.
例: CardConvertor, 用法见 CardController

**eclipse/IDEA 最好安装对应的 mapstruct 插件** 插件名称：m2e-apt
并且需要设置window-preference-Maven-Annotation Processing 选中Automatically configure JDT APT
参考https://mapstruct.org/documentation/1.1/reference/html/中2.1章解介绍

### swagger
使用 swagger 实现接口文档自动生成, 用法:
1. 在参数 bean 上加@ApiModel("案卡VO"), bean 字段上加@ApiModelProperty("创建时间")
2. Controller 层接口上加@ApiOperation, @ApiImplicitParams

例: CardVO, CardController

### junit
基于 junit 编写了 DAO, Service, Web 层的接口单元测试, 且开启了事务功能, 单元测试中对数据库的修改会自动回滚.

例: CardMapperTest, CardServiceTest, CardControllerTest

### slf4j + logback
项目日志基于 slf4j + logback, 代码中需要打印日志时统一使用slf4j的 api(门面模式). 用法示例: CardController.logger
底层日志框架实现为 logback, 另外通过配置 logback, 实现了日志文件按天打包压缩, 最多保留 30 天或 10G 的日志文件. 配置文件: logback-spring.xml 

### assembly
通过 maven 的 assmbly 插件和自定义脚本文件, 使得在项目打包(mvn clean package)时会按公司要求, 打包出规范的部署压缩包文件.配置文件: distribution.xml, 自定义脚本: src/main/resources/bin目录, 其中定义了 JVM 启动参数和其他运行时目录环境配置 

### 统一异常处理
基于 spring 的@ControllerAdvice 和 @ExceptionHandler 注解创建了ExpcetionHandler类, 统一拦截 Controller 层的异常, 并封装异常时的响应数据格式. ExpcetionHandler可以拦截@Validated参数验证未通过抛出的异常, Service 层抛出但 Controller 层未捕获的异常, Controller 层接口自己抛出的所有异常.

### 统一异常结构
封装 CardException 作为系统的统一业务异常, 可以在代码中手动抛出该异常, 该异常中封装了自定义状态码, 能够更清晰的表示异常情况, 同时上面的统一异常处理对 CardException 进行了特殊处理, 使得业务异常可以更清晰反馈给前端.

### 统一响应格式
接口统一使用 RESTfull 协议, 响应数据格式为 Result 对象的序列化之后的 json 字符串,
Service 层分页查询结果统一使用 PageResult 对象
例: CardController

### 统一响应状态码
在ResultEnum中定义不同的系统响应码, 如: 正确, 参数验证错误, 业务错误等. 开发人员在日常开发中需要不断维护该枚举, 增加状态码以适应新业务处理中的异常情况

##上海编目20210907新需求
### 新增配置
```yml
#批量编目配置
contBatchMax: 100
#连续合并证据数配置
mergeTimes: 3
#是否开启证据图片合并开关
switchFlag: true
```
### 需求详情：
```text
优化编目代码，尽量避免卡顿
```
### 需求实现
```text
1.将编目按照指定参数进行批量编目，contBatchMax控制每次进行多少张图片请求编目引擎
2.将ocr入参中不必要的字符进行剔除
3.优化证据合并规则，通过switchFlag开关控制是否将图片证据合并到前面的证据中（不区分图片证据和标题证据），mergeTimes控制往后合并多少编目结果
mergeTimes配置是在switchFlag为true的情况下生效
```