package com.henau.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、整合Mybatis-Plus
 *      1）、导入依赖
 *      <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus-boot-starter</artifactId>
 *             <version>3.2.0</version>
 *      </dependency>
 *      2）、配置
 *          1、配置数据源；
 *              1）、导入数据库的驱动；
 *              2）、在application.yml配置数据源相关信息
 *          2、配置MyBatis-Plus；
 *              1）、使用@MapperScan
 *              2）、告诉MyBatis-Plus，SQL映射文件位置
 *
 * 2、逻辑删除
 *  1）、配置全局的逻辑删除规则 （省略）
 *  2）、配置逻辑删除的组件Bean （省略）
 *  3）、加上逻辑删除注解@TableLogic
 *
 * 3、JSR303
 *  1）、给Bean添加校验注解：javax.validation.constraints
 *  2）、开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应
 *  3）、给校验的Bean后紧跟一个BindingResult，就可以获取到校验的结果
 *  4）、分组校验（多场景的复杂校验）
 *      1）、@NotBlank（message = “品牌名必须提交”，groups = {AddGroup.class, UpdateGroup.class}）
 *      给校验注解标注什么情况需要进行校验
 *      2）、@Validated({AddGroup.class})
 *      3）、默认没有指定分组的校验注解@NotBlank,在分组校验情况下不生效，只会在@Validate
 *  5）、自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器ConstraintValidator
 *      3）、关联自定义的校验器和自定义的校验注解
 *      @Documented
 *      @Constraint(
 *                 validatedBy = { ListValueConstraintValidator.class }
 *      )
 *      @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
 *      @Retention(RetentionPolicy.RUNTIME)
 * 4、统一的异常处理
 * @ControllerAdvice
 *  1）、编写异常处理类，使用@controllerAdvice。
 *  2）、使用@ExceptionHandler标注方法可以处理的异常
 *
 *  5、模板引擎
 *      1)、thymeleaf-stater： 关闭缓存
 *      2）、静态组员都放在static文件夹下就可以按照路径直接访问
 *      3)、页面放在templates下，直接访问
 *          springboot，访问项目的时候，默认会找index
 *      4)、页面修改不重启服务器实时更新
 *         1)、引入devtools
 *         2)、修改完页面 controller shift f9 重新自动编译页面,代码配置，推荐重启
 *  6、整合Redis
 *      1)、引入data-redis-starter
 *      2)、简单配置Redis的host等信息
 *      3)、使用springboot自动配置好的stringRedistemplate来操作Redis
 *          Redis-》map：存放数据，key，数值：value
 *  7、整合Redisson作为分布式锁等功能框架
 *      1）、引入依赖
 *         <dependency>
 *             <groupId>org.redisson</groupId>
 *             <artifactId>redisson</artifactId>
 *             <version>3.12.0</version>
 *         </dependency>
 *      2）、配置Redisson
 *          MyRedissonConfig给容器中配置一个RedissonClient实例即可
 *      3）、使用
 *          参照文档做。
 *  8、整合SpringCache简化缓存开发
 *      1）、引入依赖
 *          spring-boot-starter-cache
 *      2）、写配置
 *          自动配置了哪些
 *              cacheAutoConfiguration会导入RedisAutoConfiguration
 *              自动配好了缓存管理器
 *          配置使用Redis作为缓存
 *      3）、测试使用缓存
 *          @Cacheable:触发将数据保存到缓存的操作
 *          @CacheEvict:触发将数据从缓存删除的操作
 *          @CachePut:不影响方法执行更新缓存
 *          @Caching:组合以上做个操作
 *          @CacheConfig:在类级别共享缓存的相同配置
 *          开启缓存功能
 *          只需要使用注解就能够开启缓存功能
 *      4）、原理
 *          CacheAutoConfiguration -> RedisCacheConfiguration ->
 *          自动配置了RedisCacheManager->初始化所有的缓存->每个缓存决定使用什么配置
 *          ->如果RedisCacheConfiguration有就用已有的，没有就用默认配置
 *          ->想改缓存的配置，只需要给容器中放一个RedisCacheConfiguration即可
 *          ->就会应用到当前RedisCacheManager管理的所有缓存分区中
 */

@EnableFeignClients(basePackages = "com.henau.mall.product.feign")
@MapperScan("com.henau.mall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }

}
