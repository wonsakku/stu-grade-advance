package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GradeAdvanceServiceTest {

    private final States state = new States(Paths.get("target/state"));
    TargetGen mockGen = mock(TargetGen.class);
    private TargetsExporter mockExporter = mock(TargetsExporter.class);

    GradeAdvanceService service = new GradeAdvanceService(state, mockGen, mockExporter);

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get("target/state"));
    }


    @Test
    void alreadyCompleted(){
        state.set(AdvanceState.COMPLETED);
        AdvanceResult result = service.advance();
        assertThat(result).isEqualTo(AdvanceResult.ALREADY_COMPLETED);
    }


    @Test
    void targetGenFail(){
        BDDMockito.given(mockGen.gen()).willThrow(new RuntimeException("!"));

        AdvanceResult result = service.advance();
        assertThat(result).isEqualTo(AdvanceResult.TARGET_GET_FAILED);
    }

    @Test
    void targetExportFail(){
        BDDMockito.given(mockGen.gen()).willReturn(mock(Targets.class));
        BDDMockito.willThrow(new RuntimeException(("!")))
                .given(mockExporter).export(Mockito.any(Path.class), Mockito.any(Targets.class));

        AdvanceResult result = service.advance();
        assertThat(result).isEqualTo(AdvanceResult.TARGET_EXPORT_FAILED);
    }


    private class GradeAdvanceService {
        private final States states;
        private final TargetGen targetGen;
        private final TargetsExporter targetsExporter;

        public GradeAdvanceService(States state, TargetGen targetGen, TargetsExporter targetsExporter) {
            this.states = state;
            this.targetGen = targetGen;
            this.targetsExporter = targetsExporter;
        }

        public AdvanceResult advance() {
            AdvanceState state = states.get();
            if(state == AdvanceState.COMPLETED){
                return AdvanceResult.ALREADY_COMPLETED;
            }

            Targets targets;
            try {
                targets = targetGen.gen();
            } catch (Exception e) {
                return AdvanceResult.TARGET_GET_FAILED;
            }

            try {
                targetsExporter.export(Paths.get("target/targets"),targets);
            } catch (Exception e) {
                return AdvanceResult.TARGET_EXPORT_FAILED;
            }

            return null;
        };
    }

    private class TargetGen {
        public Targets gen() {
            return null;
        }
    }

    private class Targets {
    }

    private enum AdvanceResult {
        TARGET_GET_FAILED, TARGET_EXPORT_FAILED, ALREADY_COMPLETED
    }

    private class TargetsExporter {
        public void export(Path path, Targets targets) {

        }
    }
}
