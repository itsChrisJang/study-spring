package io.springbatch.springbatchlecture.job.config.sector.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ChunkConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    @Bean
//    public Job batchJob() {
//        return jobBuilderFactory.get("batchJob")
//                .start(step1())
//                .next(step2())
//                .build();
//    };
//
//    @Bean
//    public Step step1() {
//        return stepBuilderFactory.get("step1")
//                .<String, String>chunk(5)
//                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
//                .processor(new ItemProcessor<String, String>() {
//                    @Override
//                    public String process(String item) throws Exception {
//                        Thread.sleep(300);
//                        System.out.println("item = " + item);
//                        return "my" + item;
//                    }
//                })
//                .writer(new ItemWriter<String>() {
//                    @Override
//                    public void write(List<? extends String> items) throws Exception {
//                        Thread.sleep(300);
//                        System.out.println("items = " + items);
//                    }
//                })
//                .build();
//    }
//
//    @Bean
//    public Step step2() {
//        return stepBuilderFactory.get("step2")
//                .tasklet((contribution, chunkContext) -> {
//                    System.out.println("step2 has executed");
//                    return RepeatStatus.FINISHED;
//                }).build();
//    }
}
