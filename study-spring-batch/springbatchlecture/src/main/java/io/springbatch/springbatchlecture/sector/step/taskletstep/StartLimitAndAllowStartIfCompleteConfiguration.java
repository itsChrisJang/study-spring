package io.springbatch.springbatchlecture.sector.step.taskletstep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartLimitAndAllowStartIfCompleteConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*
        startLimit()
            - Step의 실행 횟수를 조정할 수 있다.
            - Step 마다 설정할 수 있다.
            - 설정 값을 초과해서 다시 실행하려고 하면 StartLimitExceededException이 발생
            - start-limit의 디폴트 값은 Integer.MAX_VALUE
        allowStartIfComplete()
            - 재시작 가능한 job 에서 Step 의 이전 성공 여부와 상관없이 항상 step 을 실행하기 위한 설정
            - 실행 마다 유효성을 검증하는 Step이나 사전 작업이 꼭 필요한 Step 등
            - 기본적으로 COMPLETED 상태를 가진 Step 은 Job 재 시작시 실행하지 않고 스킵한다.
            - allow-start-if-complete가 "true"로 설정된 stop은 항상 실행한다.
     */
    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("StepContribution = " + stepContribution + ", ChunkContext = " + chunkContext);
                        return RepeatStatus.FINISHED;
                    }
                })
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("StepContribution = " + stepContribution + ", ChunkContext = " + chunkContext);
                        throw new RuntimeException("step2 was failed");
//                        return RepeatStatus.FINISHED;
                    }
                })
                .startLimit(3)
                .build();
    }

}
