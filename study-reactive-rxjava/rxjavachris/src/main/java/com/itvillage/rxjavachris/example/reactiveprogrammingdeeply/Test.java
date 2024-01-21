package com.itvillage.rxjavachris.example.reactiveprogrammingdeeply;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Test {
    public static void main(String[] args) throws InterruptedException {
        doTask("task1")
                .subscribe(data -> log.info("# onNext: {}", data));

        doTask("task2")
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(200L);
    }

    private static Flux<Integer> doTask(String taskName) {
        return Flux.fromArray(new Integer[] {1, 3, 5, 7})
                .publishOn(Schedulers.newSingle("new-single", true))
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# {} doOnNext filter: {}", taskName, data))
                .map(data -> data * 10)
                .doOnNext(data -> log.info("# {} doOnNext map: {}", taskName, data));
         String.format("%s (%n)",target.getData(),target.getDup())

    }

    class zzzz {}

}
