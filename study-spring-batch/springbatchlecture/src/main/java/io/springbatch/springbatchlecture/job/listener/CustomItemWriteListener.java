package io.springbatch.springbatchlecture.job.listener;

import io.springbatch.springbatchlecture.entity.customer.Customer;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CustomItemWriteListener implements ItemWriteListener<Customer> {

    @Override
    public void beforeWrite(List<? extends Customer> items) {

    }

    @Override
    public void afterWrite(List<? extends Customer> items) {

        System.out.println("Thread = " + Thread.currentThread().getName() + ", write items : " + items.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Customer> items) {

    }
}
