package com.bishe.visualizationsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.bishe.visualizationsystem.admin.mapper")
@ServletComponentScan(basePackages = "com.bishe.visualizationsystem.admin")
@SpringBootApplication
public class VisualizationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisualizationSystemApplication.class, args);
    }

}
