package com.sopra.formationspringbatch.setps.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileDeletingTasklet implements Tasklet, InitializingBean {

    private String directory;

    public FileDeletingTasklet(String directory) {
        this.directory = directory;
    }

    /**
     * @param pattern
     * @param folder
     * @param result
     */
    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if ((f.isFile()) && (f.getName().matches(pattern))) {
                result.add(f.getAbsolutePath());
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(directory, "directory must be set");
    }

    /**
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        File folder = new File(this.directory);

        List<String> result = new ArrayList<>();

        search(".*\\.csv", folder, result);

        for (String filename : result) {
            File csv = new File(filename);
            boolean deleted = Files.deleteIfExists(csv.toPath());
            if (!deleted) {
                throw new UnexpectedJobExecutionException("Could not delete file " + csv.getPath());
            } else {
                System.out.println(csv.getPath() + " got deleted");
            }
        }
        return RepeatStatus.FINISHED;
    }
}
