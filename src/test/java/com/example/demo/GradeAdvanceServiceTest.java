package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class GradeAdvanceServiceTest {

    @Test
    void alreadyCompleted(){
        States state = new States(Paths.get("target/state"));
        state.set(AdvanceState.COMPLETED);

        GradeAdvanceService service = new GradeAdvanceService(state);
        Assertions.assertThatCode(
                () -> service.advance()
        ).isInstanceOf(AlreadyCompletedException.class);
    }

    private class AlreadyCompletedException extends RuntimeException{
    }

    private class GradeAdvanceService {
        private final States states;

        public GradeAdvanceService(States state) {
            this.states = state;
        }

        public void advance() {
            AdvanceState state = states.get();
            if(state == AdvanceState.COMPLETED){
                throw new AlreadyCompletedException();
            }
        }
    }
}
