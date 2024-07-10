package com.example.demo;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GradeAdvanceService {

    public static final Path DEFAULT_TARGETS_FILE = Paths.get(".", "targets");

    @Setter
    private Path targetsFilePath = DEFAULT_TARGETS_FILE;
    private States states;
    private TargetsGen targetGen;
    private TargetsExporter targetsExporter;
    private TargetsImporter targetsImporter;
    private AdvanceApplier advanceApplier;

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
        if (state == AdvanceState.COMPLETED) {
            return AdvanceResult.ALREADY_COMPLETED;
        }

        Targets targets;

        if (state == AdvanceState.APPLY_FAILED) {
            // TODO targetImporter.importTargets 가 실패하는 경우에 대한 구현 필요
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
