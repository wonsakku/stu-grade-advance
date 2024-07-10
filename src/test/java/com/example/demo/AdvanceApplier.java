package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
class AdvanceApplier {

    private JdbcTemplate jdbcTemplate;

    public AdvanceApplier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ApplyResult apply(Targets targets) {

        Map<Integer, GradeCount> gradeCntMap = new HashMap<>();

        targets.getUsers()
                .forEach(user -> {
                    int nextGrade = user.getGrade() + 1;
                    jdbcTemplate.update("UPDATE stuinfo SET grade = ? WHERE stu_id = ?",
                            nextGrade, user.getId());
                    GradeCount gradeCount = gradeCntMap.get(nextGrade);
                    if(gradeCount == null){
                        gradeCount = new GradeCount(nextGrade, 0);
                        gradeCntMap.put(nextGrade, new GradeCount(nextGrade, 1));
                    }
                    gradeCount.inc();
                });
        return new ApplyResult(gradeCntMap);
    }
}
