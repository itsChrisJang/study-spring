package io.springbatch.springbatchlecture.job;

import io.springbatch.springbatchlecture.entity.Student;
import io.springbatch.springbatchlecture.entity.StudentBackUp;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Chunk2Configuration {
    public static final String JOB_NAME = "multiThreadPagingBatch";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private int chunkSize = 1000;

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    private int poolSize= 10;

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Bean(name = JOB_NAME+"taskPool")
    public TaskExecutor executor() {
        // (2)
        // 쓰레드 풀을 이용한 쓰레드 관리 방식
        // corePoolSize: Pool의 기본 사이즈, maxPoolSize: Pool의 최대 사이즈
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

//    @Bean(name = JOB_NAME)
//    public Job job() throws Exception {
//        return jobBuilderFactory.get(JOB_NAME)
//                .start(step())
//                .preventRestart()
//                .build();
//    }

    @Bean(name = JOB_NAME +"_step")
    @JobScope
    public Step step() throws Exception {
        return stepBuilderFactory.get(JOB_NAME +"_step")
                .<Student, StudentBackUp>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(executor()) // (2)
                .throttleLimit(poolSize) // (3)
                .build();
    }

    @Bean(name = JOB_NAME +"_reader")
    @StepScope
    public JdbcPagingItemReader<Student> reader() throws Exception {

        return new JdbcPagingItemReaderBuilder<Student>()
                .name(JOB_NAME +"_reader")
                .pageSize(chunkSize)
//                .fetchSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Student.class))
                .queryProvider(createQueryProvider())
                .saveState(false)
                .build();
    }

    private ItemProcessor<Student, StudentBackUp> processor() {
//        return student -> {
//            StudentBackUp studentBackUp = new StudentBackUp(student);
//            System.out.println("studentBackUp = " + studentBackUp);
//            return studentBackUp;
//        };
        return StudentBackUp::new;
    }

    @Bean(name = JOB_NAME +"_writer")
    @StepScope
    public JdbcBatchItemWriter<StudentBackUp> writer() {
        return new JdbcBatchItemWriterBuilder<StudentBackUp>()
                .columnMapped()
                .dataSource(dataSource)
                .sql("insert into studentbackup(id, name) values (:id, :name)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        final Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("from student");
        queryProvider.setSortKeys(sortKeys);
        log.info("========================= createQueryProvider =========================");
        PagingQueryProvider object = queryProvider.getObject();
        log.info("object : {}", object);
        return object;
    }
}
