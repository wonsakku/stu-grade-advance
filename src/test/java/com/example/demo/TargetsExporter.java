package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
class TargetsExporter {
    public void export(Path path, Targets targets) {
        try(BufferedWriter bw = Files.newBufferedWriter(path)){
            List<User> users = targets.getUsers();
            for (User user : users) {
                bw.write(user.getId() + "=" + user.getGrade());
                bw.newLine();
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
