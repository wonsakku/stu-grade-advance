package com.example.demo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ApplyResult {
    private final Map<Integer, GradeCount> gradeCntMap;

    public ApplyResult(Map<Integer, GradeCount> gradeCntMap) {

        this.gradeCntMap = gradeCntMap;
    }

    public Collection<GradeCount> getGradeCounts() {
        return gradeCntMap.values();
    }
}
