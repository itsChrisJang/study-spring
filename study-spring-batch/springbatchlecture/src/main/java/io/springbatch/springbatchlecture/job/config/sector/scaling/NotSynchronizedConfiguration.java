/*
package io.springbatch.springbatchlecture.job.config;

import io.springbatch.springbatchlecture.entity.customer.Customer;
import io.springbatch.springbatchlecture.entity.customer.CustomerRowMapper;
import io.springbatch.springbatchlecture.job.listener.CustomItemReadListener;
import io.springbatch.springbatchlecture.job.partitioner.ColumnRangePartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;

@RequiredArgsConstructor
@Configuration
public class NotSynchronizedConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job notSynchronizedBatchJob() throws Exception {
        return jobBuilderFactory.get("notSynchronizedBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws InterruptedException {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(100)
                .reader(customerItemReader())
                .listener(new ItemReadListener<Customer>() {
                    @Override
                    public void beforeRead() {

                    }

                    @Override
                    public void afterRead(Customer item) {
                        System.out.println("Thread : " + Thread.currentThread().getName() + ", item.getId() : " + item.getId());
                    }

                    @Override
                    public void onReadError(Exception ex) {

                    }
                })
                .writer(customItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Customer> customerItemReader() {
        return new JdbcCursorItemReaderBuilder()
                .fetchSize(60)
                .dataSource(dataSource)
                .beanRowMapper(Customer.class)
                .sql("select id, firstName, lastName, birthDate from customer order by id")
                .name("NotSafetyReader")
                .build();
    }

    @Bean
    @StepScope  // 각 쓰레드마다 런타임 시점에 각각의 itemWriter를 생성한다. (= 병렬)
    public ItemWriter<Customer> customItemWriter() {
        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into customer_backup values (:id, :firstName, :lastName, :birthDate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);    // 기본적인 풀 갯수
        taskExecutor.setMaxPoolSize(8);     // 위에 쓰레드가 아직 작업 중일 경우 확장
        taskExecutor.setThreadNamePrefix("not-safety-thread");   // 네임 prefix

        return taskExecutor;
    }
}
*/
