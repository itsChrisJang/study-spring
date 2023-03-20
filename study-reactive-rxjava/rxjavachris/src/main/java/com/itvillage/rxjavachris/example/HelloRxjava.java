package com.itvillage.rxjavachris.example;

import io.reactivex.Observable;

public class HelloRxjava {
    public static void main(String[] args) {
        Observable<String> observable = Observable.just("Hello", "RxJava");
        observable.subscribe(data -> System.out.println(data));

    }
}
