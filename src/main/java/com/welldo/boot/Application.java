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
 * 在src/main/resources目录下，注意到几个文件：
 *
 *
 * 1.Spring Boot默认的配置文件，
 * 文件名必须是application.yml而不是其他名称。
 * YAML是一种层级格式，它的优点是去掉了大量重复的前缀，并且更加易读。
 * 也可以使用 application.properties 作为配置文件，但不如YAML格式简单。
 * 注意：
 * 冒号后面要有空格
 *
 *
 * 1.5
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
 * logback-spring.xml是Spring Boot的logback配置文件名称
 * （也可以使用logback.xml），
 *
 *
 * 3.
 * static是静态文件目录，templates是模板文件目录，
 * 注意它们不再存放在src/main/webapp下，而是直接放到src/main/resources这个classpath目录，
 * 因为在Spring Boot中已经不需要专门的webapp目录了。
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
 */
@SpringBootApplication
public class Application {

    /**
     * 1.
     * Spring Boot的特色：AutoConfiguration。
     *
     * 1.1当我们引入 spring-boot-starter-jdbc 时，启动时会自动扫描所有的 XxxAutoConfiguration：
     *      DataSourceAutoConfiguration：自动创建一个DataSource，其中配置项从application.yml的spring.datasource读取；
     *      DataSourceTransactionManagerAutoConfiguration：自动创建了一个基于JDBC的事务管理器；
     *      JdbcTemplateAutoConfiguration：自动创建了一个JdbcTemplate。
     * 因此，我们自动得到了一个DataSource、一个DataSourceTransactionManager和一个JdbcTemplate。
     *
     * 1.2类似的，当我们引入spring-boot-starter-web时，自动创建了：
     *      ServletWebServerFactoryAutoConfiguration：自动创建一个嵌入式Web服务器，默认是Tomcat；
     *      DispatcherServletAutoConfiguration：自动创建一个 DispatcherServlet；
     *      HttpEncodingAutoConfiguration：自动创建一个 CharacterEncodingFilter；
     *      WebMvcAutoConfiguration：自动创建若干与MVC相关的Bean。
     *
     * 1.3
     * 引入第三方pebble-spring-boot-starter时，自动创建了：
     *       PebbleAutoConfiguration：自动创建了一个PebbleViewResolver。
     *
     *
     *
     */
    public static void main(String[] args) throws Exception {
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
