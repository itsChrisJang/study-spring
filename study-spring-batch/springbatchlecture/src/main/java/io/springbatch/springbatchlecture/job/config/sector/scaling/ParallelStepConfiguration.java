/*
package io.springbatch.springbatchlecture.job.config.sector.scaling;

import io.springbatch.springbatchlecture.job.config.tasklet.CustomTasklet;
import io.springbatch.springbatchlecture.job.listener.StopWatchJobListerner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class ParallelStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job parallelStepBatchJob() throws Exception {
        return jobBuilderFactory.get("parallelStepBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(flow1())
//                .next(flow2())                          // => 싱글 쓰레드로 작동
                .split(taskExecutor()).add(flow2())
                .end()
                .listener(new StopWatchJobListerner())
                .build();
    };

    @Bean
    public Flow flow1() {
        TaskletStep step1 = stepBuilderFactory.get("step1")
                .tasklet(tasklet()).build();

        return new FlowBuilder<Flow>("flow1")
                .start(step1)
                .build();
    }

    @Bean
    public Flow flow2() {
        TaskletStep step2 = stepBuilderFactory.get("step2")
                .tasklet(tasklet()).build();

        TaskletStep step3 = stepBuilderFactory.get("step3")
                .tasklet(tasklet()).build();

        return new FlowBuilder<Flow>("flow2")
                .start(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return new CustomTasklet();
    }

    // 멀티 쓰레드에 대해 설정하는 영역
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);    // 기본적인 풀 갯수
        taskExecutor.setMaxPoolSize(4);     // 위에 쓰레드가 아직 작업 중일 경우 확장
        taskExecutor.setThreadNamePrefix("async-thread-");   // 네임 prefix

        return taskExecutor;
    }

}
*/
