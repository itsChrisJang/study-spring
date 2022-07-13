# 스프링 핵심 원리 - 기본편
## 진행
| 회독   | 완료일자         |
|------|--------------|
| 1    | ~ 2022-06-29 |
---

### 목차
1. 객체 지향 설계와 스프링
2. 스프링 핵심 원리 이해1 - 예제 만들기
3. 스프링 핵심 원리 이해2 - 객체 지향 원리 적용
4. 스프링 컨테이너와 스프링 빈
5. 싱글톤 컨테이너
6. 컴포넌트 스캔
7. 의존관계 자동 주입
8. 빈 생명주기 콜백
9. 빈 스코프

--- 
## TIPs
- 클래스 다이어그램은 정적
- 객체 다이어그램은 동적

## AppConfig 적용
- 애플리케이션의 전체 동작 방식을 구성(config)하기 위해, '구현 객체를 생성'하고, '연결'하는 책임을 가지는 별도의 설정 클래스를 만들었다.

## Bean 정보 세팅
### java config 세팅시에
- java config 세팅시에는 bean 등록시 factory bean 메소드를 사용한다.   <br/>
- class Case : AnnotationConfigApplicationContext

----------------------------------
#### getBeanDefinition
class [null]    <br/>
factoryBeanName=appConfig;  <br/>
factoryMethodName=memberService;    <br/>
----------------------------------

### XML config 세팅시에
- XMl config 세팅시에는 Bean에 대한 Class 가 확인 가능하다.   <br/>
- class Case : GenericXmlApplicationContext

----------------------------------
#### getBeanDefinition
class [hello.core.member.MemoryMemberRepository]    <br/>
factoryBeanName=null;   <br/>
factoryMethodName=null; <br/>
----------------------------------

## What is Singleton Pattern?
- 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
- 그래서 객체 인스턴스가 2개 이상 생성하지 못하도록 막아야 한다.
  - private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 한다.

=> 스프링 컨테이너에서는 기본적 객체를 singleton 으로 관리해준다.
=> 싱글톤 패턴에는 수많은 문제점이 있다.

## What is Singleton Container?
- 싱글톤 패턴의 문제점을 해결하면서, 객체 익스턴스를 싱글톤(1개만 생성)으로 관리한다.

## Singleton 주의점
- 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.
- 무상태(stateless)로 설계해야 한다!
  - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
  - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다!
  - 가급적 읽기만 가능해야 한다.
  - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
- 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생할 수 있다.!!

--- 
## @Configuration
### @Bean
``` java
// AppConfig@CGLIB 예상 코드
@Bean
public MemberRepository memberRepository() {
  if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
    return 스프링 컨테이너에서 찾아서 반환;
  } else { //스프링 컨테이너에 없으면
    기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
    return 반환
  }
}
```
```text
// call AppConfig.memberService
// call AppConfig.memberRepository
// call AppConfig.orderService
// bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$b5b9f6ee
```
- @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 
- 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.

### What if there is no annotated @Configuration
```text
// call AppConfig.memberService
// call AppConfig.memberRepository
// call AppConfig.orderService
// call AppConfig.memberRepository
// call AppConfig.memberRepository
// bean = class hello.core.AppConfig
```
- 싱글톤을 보장하지 않는다.

### @Configuration 정리
- @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
  - `memberRepository()` 처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
- 크게 고민할 것이 없다. 스프링 설정 정보는 항상 `@Configuration` 을 사용하자.
---

## @Autowired
- 의존 관계 자동 주입.

---
# 의존관계 자동 주입 방법
## 다양한 의존관계 주입 방법
### 생성자 주입
- 이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법이다.
- 특징
  - 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
  - 불변, 필수 의존관계에 사용
```java
    @Component
    public class OrderServiceImpl implements OrderService {
        private final MemberRepository memberRepository;
        private final DiscountPolicy discountPolicy;
        
        @Autowired
        public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    }
```
> 중요! 
> - 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입
### 수정자 주입(setter 주입)
- setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법
- 특징
  - 선택, 변경 가능성이 있는 의존관계에 사용
  - 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.
```java
    @Component
    public class OrderServiceImpl implements OrderService {
        private MemberRepository memberRepository;
        private DiscountPolicy discountPolicy;
        
        @Autowired
        public void setMemberRepository(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
        
        @Autowired
        public void setDiscountPolicy(DiscountPolicy discountPolicy) {
            this.discountPolicy = discountPolicy;
        }
    }
```
> 참고: 
> - @Autowired 의 기본 동작은 주입할 대상이 없으면 오류가 발생한다. 
> - 주입할 대상이 없어도 동작하게 하려면 @Autowired(required = false) 로 지정하면 된다.
### 필드 주입
- 이름 그대로 필드에 바로 주입하는 방법이다.
- 특징
  - 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다. 
  - DI 프레임워크가 없으면 아무것도 할 수 없다.
  - 사용하지 말자!
    - 애플리케이션의 실제 코드와 관계 없는 테스트 코드이다.
    - 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용한다.
```java
    @Component
    public class OrderServiceImpl implements OrderService {
        @Autowired
        private MemberRepository memberRepository;
        
        @Autowired
        private DiscountPolicy discountPolicy;
    }
```
> 참고1
> - 순수한 자바 테스트 코드에는 당연히 @Autowired가 동작하지 않는다. 
> - @SpringBootTest 처럼 스프링 컨테이너를 테스트에 통합한 경우에만 가능하다.

> 참고2
> - 다음 코드와 같이 @Bean 에서 파라미터에 의존관계는 자동 주입된다. 
> - 수동 등록시 자동 등록된 빈의 의존관계가 필요할 때 문제를 해결할 수 있다.
### 일반 메서드 주입
- 일반 메서드를 통해서 주입 받을 수 있다.
- 특징
  - 한번에 여러 필드를 주입 받을 수 있다.
  - 일반적으로 잘 사용하지 않는다.
```java
    @Component
    public class OrderServiceImpl implements OrderService {
        private MemberRepository memberRepository;
        private DiscountPolicy discountPolicy;
        
        @Autowired
        public void init(MemberRepository memberRepository, DiscountPolicy
                discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    }
```
> 참고: 
> - 어쩌면 당연한 이야기이지만 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야
동작한다. 
> - 스프링 빈이 아닌 Member 같은 클래스에서 @Autowired 코드를 적용해도 아무 기능도
동작하지 않는다.
---
## 옵션 처리
- 주입할 스프링 빈이 없어도 동작해야 할 때가 있다.
- 그런데 @Autowired 만 사용하면 required 옵션의 기본값이 true 로 되어 있어서 자동 주입 대상이
없으면 오류가 발생한다.

> 자동 주입 대상을 옵션으로 처리하는 방법은 다음과 같다.
> - @Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안된다.
> - org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다.
> - Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.
```java
    //호출 안됨
    @Autowired(required = false)
    public void setNoBean1(Member member) {
        System.out.println("setNoBean1 = " + member);
    }
    //null 호출
    @Autowired
    public void setNoBean2(@Nullable Member member) {
        System.out.println("setNoBean2 = " + member);
    }
    //Optional.empty 호출
    @Autowired(required = false)
    public void setNoBean3(Optional<Member> member) {
        System.out.println("setNoBean3 = " + member);
    }
    
    // 출력 결과
    // setNoBean1() 은 @Autowired(required=false) 이므로 호출 자체가 안된다

    // setNoBean2 = null
    // setNoBean3 = Optional.empty
```

## 롬복과 최신 트렌드

### @RequiredArgsConstructor
```java
@Component
public class OrderServiceImpl implements OrderService {
  private final MemberRepository memberRepository;
  private final DiscountPolicy discountPolicy;
  
  public OrderServiceImpl(MemberRepository memberRepository, 
                          DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
  }
}
```
- 롬복 라이브러리가 제공하는 @RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서
  생성자를 자동으로 만들어준다.
```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final MemberRepository memberRepository;
  private final DiscountPolicy discountPolicy;
}
```
- 최근에는 생성자를 딱 1개 두고, @Autowired 를 생략하는 방법을 주로 사용한다. 
- 여기에 Lombok 라이브러리의 @RequiredArgsConstructor 함께 사용하면 기능은 다 제공하면서, 코드는 깔끔하게 사용할 수 있다.
