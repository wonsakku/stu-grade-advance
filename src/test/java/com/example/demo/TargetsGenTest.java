package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TargetsGenTest {

    @Autowired
    private TargetsGen targetsGen;

    @Autowired
    private GivenAssertHelper helper;

    @Test
    void gen() throws  Exception{
        // given
        // students 몇 개 넣고
        helper.clearStu();
        helper.givenStu(101, 1);
        helper.givenStu(102, 2);
        helper.givenStu(103, 3);

        Targets targets = targetsGen.gen();
        assertThat(targets.getUsers()).hasSize(3);
        assertThat(targets.getUsers()).contains(
                new User(101, 1),
                new User(102, 2),
                new User(103, 3)
        );
    }
}




