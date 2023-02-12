package io.springbatch.springbatchlecture.job.config;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import io.springbatch.springbatchlecture.entity.customer.Customer;
import io.springbatch.springbatchlecture.entity.customer.CustomerRowMapper;
import io.springbatch.springbatchlecture.job.listener.CustomItemProcessListener;
import io.springbatch.springbatchlecture.job.listener.CustomItemReadListener;
import io.springbatch.springbatchlecture.job.listener.CustomItemWriteListener;
import io.springbatch.springbatchlecture.job.listener.StopWatchJobListerner;
import io.springbatch.springbatchlecture.job.partitioner.ColumnRangePartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;

@RequiredArgsConstructor
@Configuration
public class PartitioningConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /*
    @Bean
    public Job partitioningBatchJob() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(masterStep())
                .build();
    }

    @Bean
    public Step masterStep() throws InterruptedException {
        return stepBuilderFactory.get("masterStep")
                .partitioner(slaveStep().getName(), partitioner())
                .step(slaveStep())
                .gridSize(4)
                .taskExecutor(new SimpleAsyncTaskExecutor())
//                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep() throws InterruptedException {
        return stepBuilderFactory.get("slaveStep")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReader(null, null))
                .listener(new CustomItemReadListener())
//                .processor((ItemProcessor<Customer, Customer>) item -> item.renewCustom())
                .processor(customerItemProcessor())
                .writer(customItemWriter())
//                .listener(new CustomItemWriteListener())
//                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);    // 기본적인 풀 갯수
        taskExecutor.setMaxPoolSize(8);     // 위에 쓰레드가 아직 작업 중일 경우 확장
        taskExecutor.setThreadNamePrefix("async-thread");   // 네임 prefix

        return taskExecutor;
    }

    @Bean
    public Partitioner partitioner() {

        ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();

        columnRangePartitioner.setColumn("id");
        columnRangePartitioner.setDataSource(dataSource);
        columnRangePartitioner.setTable("customer");

        return columnRangePartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<Customer> pagingItemReader(
            @Value("#{stepExecutionContext['minValue']}") Long minValue,
            @Value("#{stepExecutionContext['maxValue']}") Long maxValue
    ) {

        System.out.println("minValue = " + minValue + ", to = " + maxValue);

        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(300);
        reader.setRowMapper(new CustomerRowMapper());

//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthDate");
        queryProvider.setFromClause("from customer");
        queryProvider.setWhereClause("where id >= " + minValue + " and id <= " + maxValue);

        HashMap<String , Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);

        return reader;
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
    public ItemProcessor<Customer, Customer> customerItemProcessor() throws InterruptedException {

        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                // 비동기의 타겟이 되는 부분.

                // 데이터 처리한다.
                return new Customer(item.getId(), "PROCESSED"+item.getFirstName(),
                        "PROCESSED"+item.getFirstName().toUpperCase(), item.getBirthDate());
            }
        };
    }
    */
}
