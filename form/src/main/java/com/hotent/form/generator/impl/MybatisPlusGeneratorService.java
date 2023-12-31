package com.hotent.form.generator.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.hotent.base.constants.DataSourceConsts;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.WebUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.form.generator.GeneratorModel;
import com.hotent.form.generator.GeneratorService;
import com.hotent.form.manager.FormCodegenLogManager;
import com.hotent.form.model.FormCodegenLog;

/**
 * 基于Mybatis-Plus的代码生成器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月30日
 */
@Service
public class MybatisPlusGeneratorService implements GeneratorService{
	@Resource
	DatabaseContext databaseContext;
	@Resource
	BoDefManager boDefManager;
	@Resource
	FormCodegenLogManager formCodegenLogManager;

	// 设置代码生成的数据源配置
	private DataSourceConfig getDataSource(GeneratorModel generatorModel) {
		String type = generatorModel.getType();
		String dataSourceAlias = generatorModel.getDataSourceAlias();
		String[] tableName = generatorModel.getTableName();
		String formkey = generatorModel.getFormkey();
		// 物理表模式
		if(GeneratorModel.TYPE_TABLE.equals(type)) {
			Assert.isTrue(BeanUtils.isNotEmpty(tableName), "必须传入物理表名");
		}
		// 表单模式
		else {
			Assert.isTrue(StringUtil.isNotEmpty(formkey), "必须传入表单Key");
			List<BoDef> boDefList = boDefManager.getByFormKey(formkey);
			Assert.isTrue(BeanUtils.isNotEmpty(boDefList), String.format("未获取到formkey:%s对应的业务对象", formkey));

			Set<String> tableNameSet = new HashSet<>();

			Set<String> boDataSourceAlias = new HashSet<>();
			// 此处只获取第一个BoEnt所在的数据源
			boDefList.forEach(bd -> {
				// 通过boDefManager.getByFormKey()获取的BoDef中没有构建实体信息，所以通过getByAlias再次获取一次。
				BoDef boDef = boDefManager.getByAlias(bd.getAlias());
				BoEnt boEnt = boDef.getBoEnt();
				// 添加BoEnt的物理表名
				tableNameSet.add(boEnt.getTableName());
				// 是否外部数据源
				if(boEnt.isExternal()) {
					boDataSourceAlias.add(boEnt.getDsName());
				}				
				List<BoEnt> childEntList = boEnt.getChildEntList();
				// 遍历子BoEnt
				childEntList.forEach(cbe -> {
					tableNameSet.add(cbe.getTableName());
				});
			});
			Assert.isTrue(boDataSourceAlias.size() < 2, String.format("表单formkey：%s对应的业务对象属于不同的数据源", formkey));
			if(boDataSourceAlias.size() == 1) {
				dataSourceAlias = boDataSourceAlias.parallelStream().findFirst().get();
			}
			tableName = tableNameSet.toArray(new String[0]);
		}

		// 默认连接本地数据源
		if(StringUtil.isEmpty(dataSourceAlias)) {
			dataSourceAlias = DataSourceConsts.LOCAL_DATASOURCE;
		}
		// 将数据源别名设置回generatorModel
		generatorModel.setDataSourceAlias(dataSourceAlias);
		// 将物理表名集合设置回generatorModel
		generatorModel.setTableName(tableName);

		DataSource dataSourceByAlias = databaseContext.getDataSourceByAlias(dataSourceAlias);
		String dbTypeByAlias = databaseContext.getDbTypeByAlias(dataSourceAlias);
		return new DynamicDataSourceConfig(dbTypeByAlias, dataSourceByAlias);
	}

	// 设置代码的生成目录，代码生成作者
	private GlobalConfig getGlobalConfig(String baseProjectPath, String authorName) {
		GlobalConfig globalConfig = new GlobalConfig()
				.setOutputDir(String.format("%s%s", baseProjectPath, "/src/main/java".replace("/", File.separator)))//输出目录
				.setFileOverride(true)// 是否覆盖文件
				.setActiveRecord(true)// 开启 activeRecord 模式
				.setEnableCache(false)// XML 二级缓存
				.setBaseResultMap(true)// XML ResultMap
				.setBaseColumnList(true)// XML columList
				.setOpen(false)//生成后打开文件夹
				.setAuthor(authorName)
				.setSwagger2(true)
				.setIdType(IdType.ASSIGN_ID)//ID生成规则
				// 自定义文件命名，注意 %s 会自动填充实体属性！
				.setMapperName("%sDao")
				.setXmlName("%sMapper")
				.setServiceName("%sManager")
				.setServiceImplName("%sManagerImpl")
				.setControllerName("%sController");
		return globalConfig;
	}

	// 设置代码生成时的物理表
	private StrategyConfig getStrategyConfig(String...tableNames) {
		return new StrategyConfig()
				// .setCapitalMode(true)// 全局大写命名
				//.setDbColumnUnderline(true)//全局下划线命名
				//.setTablePrefix(new String[]{prefix})// 此处可以修改为您的表前缀
				.setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
				.setInclude(tableNames) // 需要生成的表
				.setRestControllerStyle(true)
				// 自定义实体父类
				.setSuperEntityClass("com.hotent.base.entity.BaseModel")
				// 自定义 service 父类 默认IService
				.setSuperServiceClass("com.hotent.base.manager.BaseManager")
				// 自定义 service 实现类父类 默认ServiceImpl
				.setSuperServiceImplClass("com.hotent.base.manager.impl.BaseManagerImpl")
				// 自定义 controller 父类
				.setSuperControllerClass("com.hotent.base.controller.BaseController");
	}

	// 设置包信息
	private PackageConfig getPackageConfig(String basePackage, String moduleName) {
		Assert.isTrue(StringUtil.isNotEmpty(basePackage), "基础包路径(BasePackage)不能为空");
		return new PackageConfig()
				.setParent(basePackage)// 自定义包路径
				.setModuleName(moduleName)//模块名称
				.setController("controller")// 这里是控制器包名，默认 web
				.setEntity("model")
				.setMapper("dao")
				.setService("manager")
				.setServiceImpl("manager.impl")
				.setXml("mapper");
	}

	// 设置要注入的自定义属性
	private InjectionConfig getInjectionConfig(String baseProjectPath, GeneratorModel generatorModel) {
		InjectionConfig injectionConfig = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<>();
				map.put("system", generatorModel.getSystem());
				String authorEmail = generatorModel.getAuthorEmail();
				String companyName = generatorModel.getCompanyName();
				if(StringUtil.isNotEmpty(authorEmail)) {
					map.put("authorEmail", authorEmail);
				}
				if(StringUtil.isNotEmpty(companyName)) {
					map.put("companyName", companyName);
				}
				this.setMap(map);
			}
		};

		List<FileOutConfig> fileOutList = new ArrayList<>();
		
		// 指定mapper的输出目录
		fileOutList.add(new FileOutConfig("/template/mapper.xml.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return baseProjectPath + "/src/main/resources/mapper/".replace("/", File.separator) + tableInfo.getEntityName() + "Mapper.xml";
			}
		});
		
		// 配置vue代码的输出目录
		fileOutList.add(new FileOutConfig("/template/entityManager.vue.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return baseProjectPath + "/web/src/views/".replace("/", File.separator) + tableInfo.getEntityName() + "Manager.vue";
			}
		});
		injectionConfig.setFileOutConfigList(fileOutList);

		return injectionConfig;
	}

	// 设置代码生成模板
	private TemplateConfig getTemplateConfig() {
		return new TemplateConfig()
				// 默认不输出Mapper.xml文件，通过自定义输出配置生成Mapper.xml文件
				.setXml(null)
				.setController("template/controller.java")
				.setEntity("template/entity.java")
				.setMapper("template/mapper.java")
				.setService("template/service.java")
				.setServiceImpl("template/serviceImpl.java");
	}

	@Override
	@Transactional
	public String generator(GeneratorModel generatorModel) throws IOException {
		AutoGenerator gen = new AutoGenerator();
		// 1.设置数据源
		gen.setDataSource(getDataSource(generatorModel));
		String tmpdir = FileUtil.getIoTmpdir();
		String codeFolder = String.format("eipcode-%s", LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
		String baseProjectPath = String.format("%s%s",  tmpdir.endsWith(File.separator) ? tmpdir : tmpdir + File.separator, codeFolder);
		// 2.设置全局配置
		gen.setGlobalConfig(getGlobalConfig(baseProjectPath, generatorModel.getAuthorName()));
		// 3.设置策略配置
		gen.setStrategy(getStrategyConfig(generatorModel.getTableName()));
		// 4.设置包配置
		gen.setPackageInfo(getPackageConfig(generatorModel.getBasePackage(), generatorModel.getModuleName()));
		// 5.设置自定义属性
		gen.setCfg(getInjectionConfig(baseProjectPath, generatorModel));
		// 6.设置模板引擎
		gen.setTemplateEngine(new FreemarkerTemplateEngine());
		// 7.设置模板
		gen.setTemplate(getTemplateConfig());
		// 8.执行代码生成
		gen.execute();
		// 9.记录日志
		this.saveCodegenLog(generatorModel);
		return codeFolder;
	}
	
	// 记录代码生成日志
	private void saveCodegenLog(GeneratorModel generatorModel) throws IOException {
		FormCodegenLog formCodegenLog = new FormCodegenLog();
		HttpServletRequest request = HttpUtil.getRequest();
		if(BeanUtils.isNotEmpty(request)) {
			String ipAddr = WebUtil.getIpAddr(request);
			formCodegenLog.setIp(ipAddr);
		}
		String type = generatorModel.getType();
		formCodegenLog.setType(type);
		if(GeneratorModel.TYPE_FORM.equals(type)) {
			formCodegenLog.setTableOrForm(generatorModel.getFormkey());
		}
		else if(GeneratorModel.TYPE_TABLE.equals(type)) {
			formCodegenLog.setTableOrForm(StringUtil.join(generatorModel.getTableName()));
		}
		String json = JsonUtil.toJson(generatorModel);
		formCodegenLog.setOpeContent(json);
		formCodegenLogManager.create(formCodegenLog);
	}

	@Override
	public void download(HttpServletResponse response, String codeFolder) throws IOException {
		Assert.isTrue(StringUtil.isNotEmpty(codeFolder), "下载代码的目录不能为空");
		String tmpdir = FileUtil.getIoTmpdir();
		
		String baseProjectPath = String.format("%s%s",  tmpdir.endsWith(File.separator) ? tmpdir : tmpdir + File.separator, codeFolder);
		// 打包
		ZipUtil.zip(baseProjectPath, true);
		// 导出
		HttpUtil.downLoadFile(response, baseProjectPath + ".zip", codeFolder + ".zip");
		// 删除导出的文件
		FileUtil.deleteFile(baseProjectPath + ".zip");
	}
}
