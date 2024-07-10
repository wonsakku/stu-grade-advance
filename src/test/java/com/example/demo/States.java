package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
class States {
    private final Path path;

    public States(@Value("${states.path}") Path path) {
        this.path = path;
    }

    public AdvanceState get() {
        if (!Files.exists(path)) {
            return null;
        }
        try {
            List<String> lines = Files.readAllLines(path);
            return AdvanceState.valueOf(lines.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(AdvanceState state) {
        try {
            Files.write(this.path, state.name().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
