package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AdvanceApplierTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void apply() {

        clearStu();
        givenStu(101, 1);
        givenStu(102, 2);
        givenStu(103, 3);

        AdvanceApplier applier = new AdvanceApplier(jdbcTemplate);
        applier.apply(new Targets(Arrays.asList(
            new User(101, 1),
            new User(102, 2),
            new User(103, 3)
        )));

        assertStuGrade(101, 2);
        assertStuGrade(102, 3);
        assertStuGrade(103, 4);
    }


    @Test
    void applyResult() {

        clearStu();
        givenStu(101, 1);
        givenStu(102, 2);
        givenStu(103, 3);

        AdvanceApplier applier = new AdvanceApplier(jdbcTemplate);
        ApplyResult applyResult = applier.apply(new Targets(Arrays.asList(
                new User(101, 1),
                new User(102, 2)
        )));

        Collection<GradeCount> cnts = applyResult.getGradeCounts();
        assertThat(cnts).contains(new GradeCount(2, 1), new GradeCount(3, 1));
    }


    private void assertStuGrade(int id, int expGrade) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT stu_id, grade FROM stuinfo WHERE stu_id = ?", id);
        rs.next();
        assertThat(rs.getInt("grade")).isEqualTo(expGrade);
    }


    private void clearStu() {
        jdbcTemplate.update("truncate table stuinfo");
    }

    private void givenStu(int id, int grade) {
        jdbcTemplate.update("insert into stuinfo values (?, ?)", id, grade);
    }



}