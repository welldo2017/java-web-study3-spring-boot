package com.welldo.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 0.标准的springboot的Maven目录结构如下：
 * springboot
 * ├── pom.xml
 * ├── src
 * │   └── main
 * │       ├── java
 * │       └── resources
 * │           ├── application.yml
 * │           ├── logback-spring.xml
 * │           ├── static
 * │           └── templates
 * └── target
 *
 * 在src/main/resources目录下，注意到几个文件：
 *
 * 1.配置文件，
 * Spring Boot默认的配置文件，文件名必须是 application.yml。(也可以使用 application.properties)
 * yml文件内容是k: v对
 * 注意，k的冒号后面，必须要有空格
 *
 *
 *
 * 1.5 环境变量
 * 配置中，有这样的写法
 * server:
 *  port: ${APP_PORT:8080}
 * #这种${APP_PORT:8080}意思是，首先从环境变量查找 APP_PORT，如果环境变量定义了，那么使用环境变量的值，否则，使用默认值 8080。
 *
 * 这使得我们在开发和部署时更加方便，因为开发时无需设定任何环境变量，直接使用默认值，即本地数据库，
 * 而实际线上运行的时候，只需要传入环境变量即可：
 * $ DB_HOST=10.0.1.123 DB_USER=prod DB_PASSWORD=xxxx java -jar xxx.jar
 *
 *
 * 2.日志配置
 * logback-spring.xml是Spring Boot的logback配置文件的标准名称 （也可以使用logback.xml），
 *
 *
 * 3.
 * static是静态文件目录，templates是模板文件目录，
 * 它们不再存放在src/main/webapp下，而是直接放到src/main/resources这个classpath目录，
 * 因为在Spring Boot中，已经不需要专门的webapp目录了。
 *
 *
 * 4.
 * Spring Boot要求main()方法所在的启动类必须放到根package下，命名不做要求，这里我们以Application.java命名
 *
 *
 * 5.
 * 启动Spring Boot应用程序只需要一行代码加上一个注解 @SpringBootApplication，
 * 该注解实际上又包含了：
 * @SpringBootConfiguration
 *      又包含 @Configuration
 * @EnableAutoConfiguration
 *      又包含 @AutoConfigurationPackage
 * @ComponentScan
 *
 *
 * 6.pom
 * 使用Spring Boot时，强烈推荐从spring-boot-starter-parent继承，因为这样就可以引入Spring Boot的预置配置。
 * 引入了依赖spring-boot-starter-web和spring-boot-starter-jdbc，
 * 它们分别引入了Spring MVC相关依赖和Spring JDBC相关依赖，
 * 无需指定版本号，因为引入的<parent>内已经指定了
 *
 *
 */
@SpringBootApplication
public class Application {

    /**
     * 1.
     * Spring Boot的特色：AutoConfiguration。
     *
     * 1.1当我们引入 spring-boot-starter-jdbc 时，启动时会自动扫描所有的 XxxAutoConfiguration：
     *      DataSourceAutoConfiguration：自动创建一个 DataSource，其中配置项从 yml文件读取；
     *      DataSourceTransactionManagerAutoConfiguration：自动创建了一个基于JDBC的事务管理器；
     *      JdbcTemplateAutoConfiguration：自动创建了一个JdbcTemplate。
     * 因此，我们自动得到了一个DataSource、一个DataSourceTransactionManager和一个JdbcTemplate。
     * （所以，不需要手动加上 @EnableTransactionManagement ）
     *
     * 1.2类似的，当我们引入spring-boot-starter-web时，自动创建了：
     *      ServletWebServerFactoryAutoConfiguration：自动创建一个嵌入式Web服务器，默认是Tomcat；
     *      DispatcherServletAutoConfiguration：自动创建一个 DispatcherServlet；
     *      HttpEncodingAutoConfiguration：自动创建一个 CharacterEncodingFilter；(所以，不需要对 请求/响应 进行utf-8转码)
     *      WebMvcAutoConfiguration：自动创建若干与MVC相关的Bean。
     *
     * 1.3
     * 引入第三方pebble-spring-boot-starter时，自动创建了：
     *       PebbleAutoConfiguration：自动创建了一个PebbleViewResolver。
     *
     * 总结：
     * Spring Boot大量使用XxxAutoConfiguration来使得许多组件被自动化配置并创建，而这些创建过程又大量使用了Spring的Conditional功能
     *
     *
     */
    public static void main(String[] args) throws Exception {
        //启动后，在浏览器输入 http://localhost:8080 就可以直接访问页面
        SpringApplication.run(Application.class, args);
    }

    // -- Mvc configuration ---------------------------------------------------
    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
            }
        };
    }



}
