package com.marcus.startup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/21 17:36
 **/
@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.marcus"})
@MapperScan("com.marcus.*.mapper")
public class StartupApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(StartupApplication.class);
        springApplication.run(args);
    }
}
