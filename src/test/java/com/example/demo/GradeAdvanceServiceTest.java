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

    private final States states = new States(Paths.get("target/state"));
    TargetsGen mockGen = mock(TargetsGen.class);
    private TargetsExporter mockExporter = mock(TargetsExporter.class);
    private AdvanceApplier mockApplier = mock(AdvanceApplier.class);
    private TargetsImporter mockImporter = mock(TargetsImporter.class);

    GradeAdvanceService service = new GradeAdvanceService(states, mockGen, mockExporter, mockImporter, mockApplier);

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get("target/state"));
    }


    @Test
    void alreadyCompleted(){
        states.set(AdvanceState.COMPLETED);
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

    @Test
    void applyFail(){
        BDDMockito.given(mockGen.gen()).willReturn(mock(Targets.class));
        BDDMockito.given(mockApplier.apply(Mockito.any(Targets.class)))
                .willThrow(new RuntimeException("!"));

        AdvanceResult result = service.advance();
        assertThat(result).isEqualTo(AdvanceResult.TARGET_APPLY_FAILED);
    }


    @Test
    void applyFail_Then_State_ApplyFailed(){
        BDDMockito.given(mockGen.gen()).willReturn(mock(Targets.class));
        BDDMockito.given(mockApplier.apply(Mockito.any(Targets.class)))
                .willThrow(new RuntimeException("!"));

        service.advance();
        assertThat(states.get()).isEqualTo(AdvanceState.APPLY_FAILED);
    }


    @Test
    void applySuccess(){
        BDDMockito.given(mockGen.gen()).willReturn(mock(Targets.class));
        BDDMockito.given(mockApplier.apply(Mockito.any(Targets.class)))
                .willReturn(Mockito.mock(ApplyResult.class));

        AdvanceResult result = service.advance();
        assertThat(result).isEqualTo(AdvanceResult.SUCCESS);
    }

    @Test
    void state_ApplyFailed_When_Advance(){
        states.set(AdvanceState.APPLY_FAILED);
        Targets targets = new Targets(null);
        BDDMockito.given(mockImporter.importTargets(Mockito.any(Path.class)))
                        .willReturn(targets);

        service.advance();

        BDDMockito.then(mockGen).shouldHaveNoInteractions();
        BDDMockito.then(mockExporter).shouldHaveNoInteractions();
        BDDMockito.then(mockApplier).should().apply(Mockito.eq(targets));
    }


    private class GradeAdvanceService {
        private final States states;
        private final TargetsGen targetGen;
        private final TargetsExporter targetsExporter;
        private final TargetsImporter targetsImporter;
        private final AdvanceApplier advanceApplier;
        private final Path targetsFilePath = Paths.get("target/targets");

        public GradeAdvanceService(States states,
                                   TargetsGen targetGen,
                                   TargetsExporter targetsExporter,
                                   TargetsImporter targetsImporter,
                                   AdvanceApplier advanceApplier) {
            this.states = states;
            this.targetGen = targetGen;
            this.targetsExporter = targetsExporter;
            this.targetsImporter = targetsImporter;
            this.advanceApplier = advanceApplier;
        }

        public AdvanceResult advance() {
            AdvanceState state = states.get();
            if(state == AdvanceState.COMPLETED){
                return AdvanceResult.ALREADY_COMPLETED;
            }

            Targets targets;

            if(state == AdvanceState.APPLY_FAILED){
                targets = targetsImporter.importTargets(targetsFilePath);
            } else {
                try {
                    targets = targetGen.gen();
                } catch (Exception e) {
                    return AdvanceResult.TARGET_GET_FAILED;
                }

                try {
                    targetsExporter.export(targetsFilePath, targets);
                } catch (Exception e) {
                    return AdvanceResult.TARGET_EXPORT_FAILED;
                }
            }

            try {
                advanceApplier.apply(targets);
            } catch (Exception e) {
                states.set(AdvanceState.APPLY_FAILED);
                return AdvanceResult.TARGET_APPLY_FAILED;
            }

            return AdvanceResult.SUCCESS;
        }
    }

}
