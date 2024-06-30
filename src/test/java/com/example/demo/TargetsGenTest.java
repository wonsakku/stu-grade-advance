package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TargetsGenTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Test
    void gen() throws  Exception{
        // given
        // students 몇 개 넣고
        clearStu();
        givenStu(101, 1);
        givenStu(102, 2);
        givenStu(103, 3);

        TargetsGen targetsGen = new TargetsGen(jdbcTemplate);
        Targets targets = targetsGen.gen();
        assertThat(targets.getUsers()).hasSize(3);
        assertThat(targets.getUsers()).contains(
                new User(101, 1),
                new User(102, 2),
                new User(103, 3)
        );
    }

    private void clearStu() {
        jdbcTemplate.update("truncate table stuinfo");
    }

    private void givenStu(int id, int grade) {
        jdbcTemplate.update("insert into stuinfo values (?, ?)", id, grade);
    }
}




