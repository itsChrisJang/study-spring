package com.itvillage.rxjavachris.example.reactiveprogrammingdeeply;

import io.reactivex.Observable;

public class assignment_01 {
    // 1. range, filter, map을 이용하여 1부터 15 까지의 숫자 중에서 2의 배수만 필터링 한 후, 필터링 된 숫자에 제곱한 숫자를 출력하세요.
    public static void main(String[] args) {
        Observable.range(1, 15)
                .filter(number -> number % 2 == 0)
                .map(filteredNumber -> filteredNumber * filteredNumber)
                .subscribe(System.out::println);
    }
}
