package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TargetsImporterTest {

    TargetsImporter importer = new TargetsImporter();

    @Test
    void importTargets_given_noFile(){
        assertThatThrownBy(() -> {
            importer.importTargets(Paths.get("target/asdfjkl"));
        }).isInstanceOf(NoTargetsFileException.class);
    }

    @Test
    void importTargets_given_badFormatFile() throws IOException {
        FileCopyUtils.copy("105=5\n1066",
                new FileWriter(Paths.get("target/badformat").toFile()));

        assertThatThrownBy(() -> {
            importer.importTargets(Paths.get("target/badformat"));
        }).isInstanceOf(TargetsFileBadFormatException.class);
    }

    @Test
    void importTargets_given_fileExists() throws IOException {
        FileCopyUtils.copy("105=5\n106=6",
                new FileWriter(Paths.get("target/tfile").toFile()));
        Targets targets = importer.importTargets(Paths.get("target/tfile"));

        List<User> users = targets.getUsers();
        assertThat(users).hasSize(2);
        assertThat(users.get(0)).isEqualTo(new User(105, 5));
        assertThat(users.get(1)).isEqualTo(new User(106, 6));
    }


    @Test
    void importTargets_given_fileIsEmpty() throws IOException {
        FileCopyUtils.copy("",
                new FileWriter(Paths.get("target/emptyfile").toFile()));
        Targets targets = importer.importTargets(Paths.get("target/emptyfile"));

        List<User> users = targets.getUsers();
        assertThat(users).isEmpty();
    }



}








