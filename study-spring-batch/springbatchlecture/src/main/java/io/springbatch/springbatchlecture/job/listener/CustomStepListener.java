package io.springbatch.springbatchlecture.job.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CustomStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("beforeStep CURRENT THREAD : " + Thread.currentThread().getName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("afterStep CURRENT THREAD : " + Thread.currentThread().getName());
        return null;
    }
}
