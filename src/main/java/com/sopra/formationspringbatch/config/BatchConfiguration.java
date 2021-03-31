package com.sopra.formationspringbatch.config;

import com.sopra.formationspringbatch.listeners.JobExecListener;
import com.sopra.formationspringbatch.listeners.StepExecListener;
import com.sopra.formationspringbatch.setps.tasklets.FileDeletingTasklet;
import com.sopra.formationspringbatch.setps.tasklets.UncompressTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final String ARCHIVE = "src/main/resources/archive/in/examples-names.zip";

    private static final String ARCHIVE_OUT = "src/main/resources/archive/out";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job processJob() {
        return this.jobBuilderFactory.get("processJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecListener())
                .start(this.uncompress())
                .next(this.deleteFiles())
                .build();
    }

    @Bean
    public Step uncompress() {
        return this.stepBuilderFactory.get("uncompress")
                .tasklet(new UncompressTasklet(ARCHIVE, ARCHIVE_OUT))
                .listener(new StepExecListener())
                .build();
    }

    @Bean
    public Step deleteFiles() {
        return this.stepBuilderFactory.get("deleteFiles")
                .tasklet(new FileDeletingTasklet(ARCHIVE_OUT))
                .listener(new StepExecListener())
                .build();
    }
}
