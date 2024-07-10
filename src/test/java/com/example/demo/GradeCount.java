package com.example.demo;

import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode
public class GradeCount {
    private int grade;
    private int count;

    public GradeCount(int grade, int count) {
        this.grade = grade;
        this.count = count;
    }

    public void inc() {
        this.count++;
    }
}
