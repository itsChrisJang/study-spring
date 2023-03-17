package io.springbatch.springbatchlecture.sector.scaling;

import io.springbatch.springbatchlecture.entity.customer.Customer;
import io.springbatch.springbatchlecture.entity.customer.CustomerRowMapper;
import io.springbatch.springbatchlecture.job.listener.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;

@RequiredArgsConstructor
@Configuration
public class MultiThreadedConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job multiThreadedBatchJob() throws Exception {
        return jobBuilderFactory.get("multiThreadedBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
//                .start(asyncStep1())
                .listener(new StopWatchJobListerner())
                .build();
    };

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(100)
//                .listener(new CustomStepListener())
                .reader(pagingItemReader())   // 동기화 처리 가능
//                .reader(customerItemReader())   // 동기화 처리되지 않은 리더라 쓰레드가 같은 값에 접근함.
                .listener(new CustomItemReadListener())
                .processor((ItemProcessor<Customer, Customer>) item -> item)
                .listener(new CustomItemProcessListener())
                .writer(customItemWriter())
                .listener(new CustomItemWriteListener())
//                .taskExecutor(new SimpleAsyncTaskExecutor()) // 멀티 쓰레드 설정 : 고정 값
                .taskExecutor(taskExecutor()) // 멀티 쓰레드 설정 : 여러 정보 세팅 가능
                .listener(new CustomStepListener())
                .build();
    }

    // 멀티 쓰레드에 대해 설정하는 영역
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);    // 기본적인 풀 갯수
        taskExecutor.setMaxPoolSize(8);     // 위에 쓰레드가 아직 작업 중일 경우 확장
        taskExecutor.setThreadNamePrefix("async-thread");   // 네임 prefix

        return taskExecutor;
    }

    @Bean
    public JdbcCursorItemReader<Customer> customerItemReader() {
        return new JdbcCursorItemReaderBuilder()
                .name("jdbcCursorItemReader")
                .fetchSize(100)
                .sql("select id, firstName, lastName, birthDate from customer order by id")
                .beanRowMapper(Customer.class)
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step asyncStep1() throws InterruptedException {
        return stepBuilderFactory.get("asyncStep1")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReader())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Customer, Customer> customerItemProcessor() throws InterruptedException {

        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                // 비동기의 타겟이 되는 부분.

                Thread.sleep(1000);

                return new Customer(item.getId(), item.getFirstName(),
                        item.getFirstName().toUpperCase(), item.getBirthDate());
            }
        };
    }

    @Bean
    public ItemWriter asyncItemWriter() {
        AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(customItemWriter());

        return asyncItemWriter;
    }

    @Bean
    public AsyncItemProcessor asyncItemProcessor() throws InterruptedException {

        AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(customerItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return asyncItemProcessor;
    }

    @Bean
    public ItemReader<Customer> pagingItemReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(300);
        reader.setRowMapper(new CustomerRowMapper());

//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthDate");
        queryProvider.setFromClause("from customer");

        HashMap<String , Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);

        return reader;
    }

    @Bean
    public ItemWriter<Customer> customItemWriter() {
        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into customer_backup values (:id, :firstName, :lastName, :birthDate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

}
