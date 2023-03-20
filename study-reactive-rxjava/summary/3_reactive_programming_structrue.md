# Kevin의 알기 쉬운 RxJava 1부
## 3.리액티브 프로그래밍 구성 요소와 친해지기

### Reactive Streams란??
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
    - 소비자는 구독 시점과 상관없이 통지된 데이터를 처음부터 전달 받을 수 있따.
- Hot Publisher(뜨거운 생산자) : 
    - 생산자는 소비자 수와 상관없이 데이터를 한번만 통지한다.
    - **즉, 데이터를 통지하는 타임라인은 하나이다.**
    - 소비자는 발행된 데이터를 처음부터 전달 받는게 아니라 구독한 시점에 통지된 데이터들만 전달 받을 수 있다.

### Observable과 Flowable에 대한 이해
#### Observable과 Flowable의 비교
| Flowable                        | Observable                              |
|---------------------------------|-----------------------------------------|
| Reactive Streams 인터페이스를 구현하지 않음 | Reactive Streams 인터페이스를 구현함             |
| Observer 에서 데이터를 처리한다.          | Subscriber 에서 데이터를 처리한다.                |
| 데이터 개수를 제어하는 **배압 기능이 없음**      | 데이터 개수를 제어하는 **배압 기능이 있음**              |
| 배압 기능이 없기 때문에 데이터 개수를 제어할 수 없다. | Subscription 으로 전달 받는 데이터 개수를 제어할 수 있다. |
| Disposable 로 구독을 해지한다.          | Subscription 으로 구독을 해지한다.               |

#### 배압(Back Pressure)이란?
- Flowable 에서 데이터를 통지하는 속도가 Subscriber 에서 통지된 데이터를 전달받아 처리하는 속도 보다 빠를 때 밸런스를 맞추기 위해 데이터 통지량을 제어하는 기능을 말한다.
![publisher_subscriber_process](../img/back_pressure.png)
> - doOnNext() : 
>   - interval 함수에서 데이터를 통제할 때 호출되는 callBack 함수
>   - interval 함수에서 어떻게 출력이 되는지 doOnNext() 에서 확인할 수 있음.
> - observeOn()
>   - 데이터를 처리하는 Thread 를 분리할 수 있다.
>   - Schedulers.~
