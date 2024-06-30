package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TargetsExporterTest {


    @Test
    void name() throws IOException {
        TargetsExporter exporter = new TargetsExporter();
        exporter.export(Paths.get("target/stulist"),
            new Targets(Arrays.asList(
                new User(101, 1),
                new User(102, 2)
        )));

        List<String> lines = Files.readAllLines(Paths.get("target/stulist"));
        assertThat(lines.get(0)).isEqualTo("101=1");
        assertThat(lines.get(1)).isEqualTo("102=2");
    }

}