/**
 * projectName: fendo-plus-boot
 * fileName: MybatisPlusConfig.java
 * packageName: com.fendo.mybatis.plus.config
 * date: 2018-01-12 23:13
 * copyright(c) 2017-2020 xxx公司
 */
package raptor.streaming.server.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author azhe
 * @description: Mybatis-plus配置类
 * @since 2020-12-01
 **/
@Configuration
@MapperScan("raptor.streaming.server.dao")
public class MybatisPlusConfig {


  /**
   * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
    return interceptor;
  }
}
