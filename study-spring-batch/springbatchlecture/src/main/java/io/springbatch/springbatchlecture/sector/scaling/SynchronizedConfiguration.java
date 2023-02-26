package io.springbatch.springbatchlecture.sector.scaling;/*
package io.springbatch.springbatchlecture.job.config;

import io.springbatch.springbatchlecture.entity.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class SynchronizedConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job synchronizedBatchJob() throws Exception {
        return jobBuilderFactory.get("synchronizedBatchJob")
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
    public SynchronizedItemStreamReader customerItemReader() {
        JdbcCursorItemReader notSafetyReader = new JdbcCursorItemReaderBuilder()
                .fetchSize(60)
                .dataSource(dataSource)
                .beanRowMapper(Customer.class)
                .sql("select id, firstName, lastName, birthDate from customer order by id")
                .name("NotSafetyReader")
                .build();

        return new SynchronizedItemStreamReaderBuilder<Customer>()
                .delegate(notSafetyReader)
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

        System.out.println("write!");
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
