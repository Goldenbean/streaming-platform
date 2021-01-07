/**
 * projectName: fendo-plus-boot fileName: SwaggerConfig.java packageName: com.gzsys.common.config
 * date: 2018-02-01 1:14 copyright(c) 2017-2020 xxx公司
 */
package raptor.streaming.server.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author azhe
 * @description: SwaggerConfig配置类
 * @since 2020-12-01
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
//                .groupName("基础模块")
        .select()
        //加了ApiOperation注解的方法，生成接口文档
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        //可以根据url路径设置哪些请求加入文档，忽略哪些请求
        .paths(PathSelectors.any())
        .build();

  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("流计算平台V2.0 API文档")
        .version("2.0")
        .build();
  }

}
