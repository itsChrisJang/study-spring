package io.springbatch.springbatchlecture.sector.chunk;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomerItemWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        items.forEach(item -> System.out.println(item));
    }
}
