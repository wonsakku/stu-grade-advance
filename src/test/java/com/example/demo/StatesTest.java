package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StatesTest {

    private final Path path = Paths.get("target/state");
    States states = new States(path);

    @BeforeEach
    void setUp() throws IOException {
        // 테스트간의 영향을 주고받지 않기 위해 파일 삭제
        Files.deleteIfExists(path);
    }

    @Test
    void noStateFile(){
        AdvanceState state = states.get();
        assertThat(state).isNull();
    }

    @Test
    void set() throws IOException {
        states.set(AdvanceState.GERNERATING);

        List<String> lines = Files.readAllLines(Paths.get("target/state"));
        assertThat(lines.get(0)).isEqualTo(AdvanceState.GERNERATING.name());
    }

    @Test
    void get() throws IOException {

        FileCopyUtils.copy(AdvanceState.GERNERATING.name(), new FileWriter(path.toFile()));

        AdvanceState state = states.get();

        assertThat(state).isEqualTo(AdvanceState.GERNERATING);
    }

}
