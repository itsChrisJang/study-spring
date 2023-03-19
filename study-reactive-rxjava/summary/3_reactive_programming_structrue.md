# Kevin의 알기 쉬운 RxJava 1부
## 3.리액티브 프로그래밍 구성 요소와 친해지기

### Reactive Streams란?
- 리액티브 프로그래밍 라이브러리의 표준 사양이다.
- 리액티브 프로그래밍에 대한 인터페이스만 제공한다.
- RxJava는 이 Reactive Streams의 인터페이스들을 구현한 구현체임.
- Reactive Streams는 Publisher, Subscriber, Subscription, Processor 라는 4개의 인터페이스를 제공한다.
    - Publisher : 데이터를 생성하고 통지한다.
    - Subscriber : 통지된 데이터를 전달받아서 처리한다.
    - Subscription : 전달 받을 데이터의 개수를 요청하고 구독을 해지한다.
    - Processor : Publisher와 Subscriber의 기능이 모두 있음.

#### Publisher와 Subscriber간의 프로세스 흐름
![publisher_subscriber_process](../img/publisher_subscriber_process.png)

#### Cold Publisher & Hot Publisher
- Cold Publisher(차가운 생산자) : 
    - 생산자는 소비자가 구독할 때마다 데이터를 처음부터 새로 통지한다.
    - 데이터를 통지하는 새로운 타임 라인이 생성된다.
    - 소비자는 구독 시점과 상관ㅇ벗이 통지된 데이터를 처음부터 전달 받을 수 있따.
- Hot Publisher(뜨거운 생산자) : 
    - 생산자는 소비자 수와 상관없이 데이터를 한번만 통지한다.
    - **즉, 데이터를 통지하는 타임라인은 하나이다.**
    - 소비자는 발행된 데이터를 처음부터 전달 받는게 아니라 구독한 시점에 통지된 데이터들만 전달 받을 수 있다.