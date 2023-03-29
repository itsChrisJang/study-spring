# Kevin의 알기 쉬운 RxJava 1부
## 5. 리액티브 연산자(Reactive Operators)에 익숙해지기

### 리액티브 연산자 개요 및 생성 연산자
#### RxJava의 연산자(Operator)란?
- RxJava에서의 연산자는 메서드(함수)다.
- 연산자를 이용하여 데이터를 생성하고 통지하는 Flowable 이나 Observable 등의 생산자를 생성할 수 있다.
- Flowable 이나 Observable 에서 통지한 데이터를 다양한 연산자를 사용하여 가공 처리하여 결과값을 만들어 낸다.
- 연산자의 특성에 따라 카테고리로 분류된다.
  
#### Flowable/Observable 생성 연산자
- **interval**
  - 지정된 시간 간격마다 0부터 시작하는 숫자(Long)를 통지한다.
  - initialDelay 파라미터 이용해서 최초 통지에 대한 대기 시간을 지정할 수 있다.
  - 완료 없이 계속 통지한다.
  - 호출한 스레드와는 별도의 스레드에서 실행된다.
  - polling 용도의 작업을 수행할 때 활용할 수 있다.
    ![interval](../img/interval.png)
- **range**
  - 지정한 값(n) 부터 m 개의 숫자(Integer)를 통지한다.
  - for, while 문 등의 반복문을 대체할 수 있다.
    ![range](../img/range.png)
- **timer**
  - 지정한 시간이 지나면 0(Long)을 통지한다.
  - 0을 통지하고 onComplete() 이벤트가 발생하여 종료한다.
  - 호출한 스레드와는 별도의 스레드에서 실행된다.
  - 특정 시간에 대기한 후에 어떤 처리를 하고자 할 때 활용할 수 있다.
    ![timer](../img/timer.png)
