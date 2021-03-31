package com.sopra.formationspringbatch.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobExecListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Called beforeJob().");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Called afterJob().");
    }
}