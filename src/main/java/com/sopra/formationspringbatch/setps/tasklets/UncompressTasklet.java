package com.sopra.formationspringbatch.setps.tasklets;

import com.sopra.formationspringbatch.utils.UnzipFile;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class UncompressTasklet implements Tasklet {

    // "src/main/resources/archive/in/examples-names.zip"
    private final String file;

    // "src/main/resources/archive/out"
    private final String destination;

    public UncompressTasklet(final String file, final String destination) {
        this.file = file;
        this.destination = destination;
    }

    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        try {
            UnzipFile.unzip(this.file, this.destination);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unzip file: " + file, e);
        }

        return RepeatStatus.FINISHED;
    }


}
