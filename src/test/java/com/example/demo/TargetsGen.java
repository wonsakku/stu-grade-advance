package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TargetsGen {
    private final JdbcTemplate jdbcTemplate;

    public TargetsGen(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    public Targets gen() {
        List<User> users = jdbcTemplate.query("select * from stuinfo",
                (rs, rowNum) -> new User(rs.getInt("stu_id"), rs.getInt("grade"))
        );

        return new Targets(users);
    }
}
