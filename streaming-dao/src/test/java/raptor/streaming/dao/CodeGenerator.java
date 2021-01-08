package raptor.streaming.dao;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Created by azhe on 2020-12-01 15:58
 */
public class CodeGenerator {


  @Test
  public void testGenerator() {

    String projectPath = System.getProperty("user.dir");

    //1、全局配置
    GlobalConfig config = new GlobalConfig();
    config.setActiveRecord(true)//开启AR模式
        .setOpen(false)
        //生成路径(一般都是生成在此项目的src/main/java下面)
        .setOutputDir(projectPath + "/src/main/java")
        .setAuthor("azhe")//设置作者
        .setFileOverride(true)//第二次生成会把第一次生成的覆盖掉
        .setIdType(IdType.AUTO)//主键策略
        .setServiceName("%sService")//生成的service接口名字首字母是否为I，这样设置就没有I
        .setMapperName("%sDao")
        .setEntityName("%sEntity")
        .setSwagger2(true)
        .setBaseResultMap(true)//生成resultMap
        .setBaseColumnList(true);//在xml中生成基础列

    //2、数据源配置
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDbType(DbType.MYSQL)//数据库类型
        .setDriverName("com.mysql.jdbc.Driver")
        .setTypeConvert(new MySqlTypeConvert() {
          @Override
          public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
            if (fieldType.toLowerCase().contains("blob")) {
              return DbColumnType.BYTE_ARRAY;
            }
            return (DbColumnType) super.processTypeConvert(config, fieldType);

          }

        })
        .setUrl("jdbc:mysql://stream-2:3306/streaming?useUnicode=true&useSSL=false&characterEncoding=utf8")
        .setUsername("root")
        .setPassword("123456");

    //4、包名策略配置
    PackageConfig packageConfig = new PackageConfig();
    packageConfig.setParent("raptor.streaming")//设置包名的parent
        .setModuleName("server")
        .setMapper("dao")
        .setService("service")
        .setController("controller")
        .setEntity("entity");

    //3、策略配置
    StrategyConfig strategyConfig = new StrategyConfig();
    strategyConfig.setCapitalMode(true)//开启全局大写命名
        .setNaming(NamingStrategy.underline_to_camel)//下划线到驼峰的命名方式
        .setTablePrefix("ops_")//表名前缀
        .setSuperEntityClass("raptor.streaming.dao.entity.BaseEntity")
        .setEntityTableFieldAnnotationEnable(true)
        .setLogicDeleteFieldName("deleted")
        .setSuperEntityColumns("id", "modifier", "creater", "gmt_create", "gmt_modify", "remark")
        .setEntityLombokModel(false)//使用lombok
        .setRestControllerStyle(true)
        .setInclude(
            "ops_task"
//            "dev_file_system"
//            "dev_job_file"
//            "dw_source","dw_table"
//            "sys_cluster","sys_project","sys_user"
        );//逆向工程使用的表
//        .setInclude("dev_job_file");//逆向工程使用的表

    // 自定义配置
    InjectionConfig cfg = new InjectionConfig() {
      @Override
      public void initMap() {
        // to do nothing
      }
    };

    // 如果模板引擎是 freemarker
    String templatePath = "/templates/mapper.xml.vm";

    // 自定义输出配置
    List<FileOutConfig> focList = new ArrayList<>();
    // 自定义配置会被优先输出
    focList.add(new FileOutConfig(templatePath) {
      @Override
      public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        return projectPath + "/src/main/resources/mapper/" + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
      }


    });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
    cfg.setFileOutConfigList(focList);
    // 配置模板
    TemplateConfig templateConfig = new TemplateConfig();

    // 配置自定义输出模板
    //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
    // templateConfig.setEntity("templates/entity2.java");

    templateConfig.setMapper(null);
//    不重新生成service
    templateConfig.setService(null);
    //    不重新生成serviceImpl
    templateConfig.setServiceImpl(null);
    //    不重新生成Controller
    templateConfig.setController(null);
    templateConfig.setXml(null);

//        .setXml("mapper");//设置xml文件的目录
    //5、整合配置

    AutoGenerator autoGenerator = new AutoGenerator();
    autoGenerator.setGlobalConfig(config)
        .setDataSource(dataSourceConfig)
        .setStrategy(strategyConfig)
        .setCfg(cfg)
        .setTemplate(templateConfig)
        .setPackageInfo(packageConfig);

    //6、执行
    autoGenerator.execute();
  }
}
