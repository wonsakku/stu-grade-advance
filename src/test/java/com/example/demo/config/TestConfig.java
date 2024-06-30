package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {


//    @Autowired
//    private DataSource dataSource;
//
//
//    @PostConstruct
//    public void hello(){
//        System.out.println("^&*^&*^&*^&*^*&^&*^&*^&&*");
//        System.out.println("hello world");
//        initDatabase();
//    }
//
//    private void initDatabase() {
//        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(new ClassPathResource("/schema.sql"));
//        DatabasePopulatorUtils.execute(resourceDatabasePopulator, dataSource);
//        ResourceDatabasePopulator resourceDatabasePopulator2 = new ResourceDatabasePopulator(new ClassPathResource("/procedure.sql"));
//        DatabasePopulatorUtils.execute(resourceDatabasePopulator2, dataSource);
//    }

}
