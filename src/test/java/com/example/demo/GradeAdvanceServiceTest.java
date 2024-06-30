package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GradeAdvanceServiceTest {

    private final States state = new States(Paths.get("target/state"));
    TargetGen mockGen = mock(TargetGen.class);
    GradeAdvanceService service = new GradeAdvanceService(state, mockGen);

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


    private class GradeAdvanceService {
        private final States states;
        private final TargetGen targetGen;

        public GradeAdvanceService(States state, TargetGen targetGen) {
            this.states = state;
            this.targetGen = targetGen;
        }

        public AdvanceResult advance() {
            AdvanceState state = states.get();
            if(state == AdvanceState.COMPLETED){
                return AdvanceResult.ALREADY_COMPLETED;
            }

            try {
                targetGen.gen();
            } catch (Exception e) {
                return AdvanceResult.TARGET_GET_FAILED;
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
        TARGET_GET_FAILED, ALREADY_COMPLETED
    }
}
