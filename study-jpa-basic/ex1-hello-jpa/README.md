### 프록시의 특징
- 프로시 객체는 처음 사용할 때 한 번만 초기화
- 프로시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
- 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함(== 비교 실패, 대신 instance of 사용)
- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
- 영속성 컨텍스트의 도움을 받을 수 없는 준 영속 상태일 때, 프록시를 초기화 하는 문제 발생
  - 하이버 네이트는 org.hibernate.LazyInitializationException 예외를 터트림


### 프록시와 즉시로딩 주의
- **가급적 지연 로딩만 사용(특히 실무에서)**
- 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
- **즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
- **@ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정**
- @OneToMany, @ManyToMany는 기본이 지연 로딩

### 영속성 전이: CASCADE
- 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때
- EX) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장
- 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
- 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
- 종류
  - **ALL: 모두 적용**
  - **PERSIST: 영속**
  - **REMOVE: 삭제**
  - MERGE: 병합
  - REFRESH: REFRESH
  - DETACH: DETACH
  
#### 고아 객체
- 고아 객체 제거: 부모 엔티티와 연관 관계가 끊어진 자식 엔티티를 자동으로 삭제
- **orphanRemoval = true**
```java
    Parent parent1 = em.find(Parent.class, id);
    parent1.getChildren().remove(0);
    // 자식 엔티리를 컬렉션에서 제거
```
- DELETE FROM CHILD WHERE ID = ?

#### 고아 객체 - 주의
- 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
- **참조하는 곳이 하나일 때 사용해야함!**
- **특정 엔티티가 개인 소유할 때 사용**
- @OneToOne, @OneToMany만 가능
- 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화하면 부모를 제거할 때 자식도 함께 제거된다. 이것은 CascadeType.REMOVE처럼 동작한다.

### 영속성 전이 + 고아 객체, 생명 주기
- **CascadeType.ALL + orphanRemoval = true**
- 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
- 두 옵션을 모두 활성화하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
- 도메인 주도 설계(DDD)의 Aggregate Root 개념을 구현할 때 유용함

---
## 값 타입
### JPA의 데이터 타입 분류
- **엔티티 타입**
  - @Entity로 정의하는 객체
  - 데이터가 변해도 식별자로 지속해서 추적 가능
  - 예) 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능
- **값 타입**
  - int, Integer, String철머 단순히 값으로 사용하는 자바 기본 타입이나 객체
  - 식별자가 없고 값만 있으므로 변경시 추적 불가
  - 예) 숫자 100을 200으로 변경하면 완전히 다른 값으로 대체 

### 값 타입 분류
- **기본값 타입**
  - 자바 기본 타입(int, double)
  - 래퍼 클래스(Integer, Long)
  - String
  - 특징
    - 생명주기를 엔티티의 의존
      - 예) 회원을 삭제하면 이름, 나이 필드로 함께 삭제
    - 값 타입은 공유하면 X
      - 예) 회원 이름 변경시 다른 회원의 이름도 함께 변경되면 안됨
  - **참고: 자바의 기본 타입은 절대 공유 X**
    - int, doubld 같은 기본 타입(primitive type)은 절대 공유 X
    - 기본 타입은 항상 값을 복사함
    - Integer같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경 X
- **임베비드 타입**(embedded type, 복합 값 타입)
  - 특징
    - 새로운 값 타입을 직접 정의할 수 있음
    - JPA는 임베디드 타입(embedded type)이라 함
    - 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함
    - int, String과 같은 값 타입
  - 사용법
    - @Embeddable : 값 타입을 정의하는 곳에 표시
    - @Embedded : 값 타입을 사용하는 곳에 표시
    - 기본 생성자 필수
  - 장점
    - 재사용
    - 높은 응집도
    - Period.isWork()처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음
    - 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함
  - 임베디드 타입과 테이블 매핑
    - 임베디드 타입은 엔티티의 값일 뿐이다.
    - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
    - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
    - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음
  - 임베디드 타입과 null
    - 임베디드 타입의 값이 null이면 매핑한 컬럼 값은 모두 null
- **컬렉션 값 타입**(collection value type)
  
### 값 타입과 불변 객체
- 값 타입은 복잡한 객체 세상을 조금이라도 단순화하려고 만든 개념이다.
  - 따라서 값 타입은 단순하고 안전하게 다룰 수 있어야 한다.
- **값 타입 공유 참조**
  - 임베디드 타입 값은 값 타입을 여러 엔티티에서 공유하면 위험함.
  - 부작용(side effect) 발생
- **값 타입 복사**
  - 값 타입의 실제 인스터스인 값을 공유하는 것은 위험하다.
  - 대신 값(인스턴스)를 복사해서 사용해야 한다.
- **객체 타입의 한계**
  - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
  - 문제는 임베디드 타입처럼 **직접 정의한 값 타입응ㄴ 자바의 기본 타입이 아니라 객체 타입** 이다.
  - 자바 기본 타입에 값을 대입하면 값을 복사한다.
  - **객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.**
  - **객체의 공유 참조는 피할 수 없다.**
  - 예시
    - ```java
      // 기본 타입(primitive type)
      int a = 10;
      int b = a;   // 기본 타입은 값을 복사
      b = 4;
      ```
    - ```java
      // 객체 타입
      Address a = new Address("Old");
      Address b = a;
      b.setCity("New");
      ``` 
- **불변 객체**
  - 객체 타입을 수정할 수 없게 만들면 **부작용을 원천 차단**
  - **값 타입은 불변 객체(immutable object)로 설계해야함**
  - **불변 객쳬: 생성 시점 이후 절대 값을 변경할 수 없는 객체**
  - 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨
  - 참고: Integer, String은 자바가 제공하는 대표적인 불변 객체

### 값 타입의 비교
- 값 타입: 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야함.
- ```java
  int a = 10;
  int b = 10;
  ```
- ```java
  Address address1 = new Address("서울시");
  Address address2 = new Address("서울시");
  ```
- **동일성(identity)** 비교 : 인스턴스의 참조 값을 비교, == 사용
- **동등성(equivalence)** 비교 : 인스턴스의 값을 비교, equals() 사용
- 값 타입은 a.equals(b)를 사용해서 동등성 비교 해야함.
- 값 타입의 equals() 메소드를 적절하게 재정의(주로 모든 필드 사용)

### 값 타입 컬렉션
- 값 타입을 하나 이상 저장할 때 사용
- @ElementCollection, @CollectionTable 사용
- 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
- 컬렉션을 저장하기 위한 별도의 테이블이 필요하다.
> 참고: 값 타입 컬렉션은 영속성 전이(Cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다.
- 값 타입 컬렉션의 제약사항
  1. 값 타입은 엔티티와 다르게 식별자 개념이 없다.
  2. 값은 변경하면 추적이 어렵다.
     3. 값 타입 컬렉션  변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
  4. 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야 함: **null 입력X, 중복 저장X**
- 값 타입 컬렉션 대안
  1. 실무에서는 상황에 따라 **값 타입 컬렉션 대신에 일대다 관계를 고려**
  2. 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
  3. 영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
  4. EX) AddressEntity

> #### 값 타입 정리
> - **엔티티 타입의 특징**
>   - 식별자 O
>   - 생명 주기 관리
>   - 공유
> - **값 타입의 특징**
>   - 식별자 X
>   - 생명 주기를 엔티티에 의존
>   - 공유하지 않는 것이 안전(복사해서 사용)
>   - 불변 객체로 만드는 것이 안전
>   - 그리고
>     - 값 타입은 정말 값 타입이라 판단될 떄만 사용
>     - 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안됨
>     - 식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것은 값 타입이 아닌 엔티티!

---

## 객체지향 쿼리 언어1 
> ### JPA는 다양한 쿼리 방법을 지원
> - **JPQL**
> - JPA Criteria
> - **QueryDSL**
> - 네이티브 SQL
> - JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함꼐 사용

#### JPQL 소개
- 가장 단순한 조회 방법
  - EntityManager.find()
  - 객체 그래프 탐색(a.getB().getC())
- **나이가 18살 이상인 회원을 모두 검색하고 싶다면?**

#### JPQL
- JPA를 사용하면 엔티티 객체를 중심으로 개발
- 문제는 검색 쿼리
- 검색을 할 때도 **테이블이 아닌 엔티티 객체를 대상으로 검색**
- 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
- 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요
- JPA는 SQL을 추상화한 JPQL 이라는 객체 지향 쿼리 언어 제공
- SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
- **JPQL은 엔티티 객체를 대상으로 쿼리**
- **SQL은 데이터베이스 테이블을 대상으로 쿼리**\
- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- SQL을 추상화해서 특정 데이터베이스 SQL에 의존 X
- JPQL을 한마디로 정의하면 **객체 지향 SQL**

#### JPA Criteria
```java
// JPA Criteria 사용 준비
CriteriaBuilder c b = em.getCriteriaBuilder();
CriteriaQuery<Member> query = cb.createQuery(Member.class);

// 루트 클래스 (조회를 시작할 클래스)
Root<Member> m = query.from(Member.class);

// 쿼리 생성 
CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
List<Member> resultList = em.createQuery(cq).getResultList();
```
- 소개
  - 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
  - JPQL 빌더 역할
  - JPA 공식 기능
  - **단점 : 너무 복잡하고 실용성이 없다.**
  - Criteria 대신에 **QueryDSL 사용 권장**

#### QueryDSL 소개
```java
// JPQL
// select m from member m where m.age > 18
JPAFactoryQuery Query = new JPAFactoryQuery(em);
QMember m = QMember.member;

List<Member> result = query.selectFrom(m).where(m.age.gt(18)).orderBy(m.name.desc()).fetct(); 
```
- 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
- JQPL 빌더 역할
- 컴파일 시점에 문법 오류를 찾을 수 있음
- 동적쿼리 작성 편리함
- **단순하고 쉬움**
- **실무 사용 권장**

#### 네이티브 SQL 소개
```java
String sql = "SELECT ID, AGE, TEAM_ID, NAME FROM MEMBER WHERE NAME = 'kim'";

List<Member> resultList = em.createNativeQuery(sql, Member.class).getResultList();
```
- JPA가 제공하는 SQL을 직접 사용하는 기능
- JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
- 예) 오라클 CONNECT BY, 특정 DB만 사용하는 SQL 힌트

#### JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함꼐 사용
- JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, MyBatis 등을 함꼐 사용 가능
- 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요
- 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시 

### 기본 문법과 쿼리 API
#### JPQL 소개
- JPQL은 객체지향 쿼리 언어이다.
  - 따라서 테이블을 대상으로 쿼리하는 것이 아니라 **엔티티 객체를 대상으로 쿼리**한다.
- JPQL은 SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.
- JPQL은 결국 SQL로 변환된다.
> #### JPQL 문법
> ```java
> select_문 :: = 
>   select_절
>   from_절
>   [where_절]
>   [groupby_절]
>   [having_절]
>   [orderby_절]
> 
> update_문 :: = update_절 [where_절]
> delete_문 :: = delete_절 [where_절]
> ``` 
- select m from Member as m where m.age > 18
- 엔티티와 속성은 대소문자 구분 O (Member, age)
- JPQL 키워드는 대소문자 구분 X (SELECT, FROM, WHERE)
- 엔티티 이름 사용, 테이블 이름이 아님(Member)
- **별칭은 필수(m)** (as는 생략가능) 

#### TypeQuery, Query
- TypeQuery : 반환 타입이 명확할 때 사용
- Query : 반환 타입이 명확하지 않을 때 사용

#### 결과 조회 API
- query.getResultList() : **결과가 하나 이상일 떄**, 리스트 반환
  - 결과가 없으면 빈 리스트 반환
- query.getSingleResult() : **결과가 정확히 하나**, 단일 객체 반환
  - 결과가 없으면: javax.persistence.NoResultException
  - 둘 이상이면: javax.persistence.NonUniqueResultException
- 파라미터 바인딩 - 이름 기준 ,위치 기준

### 프로젝션(SELECT)
- SELECT 절에 조회할 대상을 지정하는 것
- 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
- SELECT **m** FROM Member m -> 엔티티 프로젝션
- SELECT **m.team** FROM Member m -> 엔티티 프로젝션
- SELECT **m.address** FROM Member m -> 임베디드 타입 프로젝션
- SELECT **m.username, m,age** FROM Member m -> 스칼라 타입 프로젝션
- DISTINCT로 중복 제거

#### 프로젝션 - 여러 값 조회
- SELECT **m.username, m.age** FROM Member m
  1. Query 타입으로 조회
  2. Object[] 타입으로 조회
  3. new 명령어로 조회
     - 단순 값을 DTO로 바로 조회
       - SELECT **new** jpabook.jpql.UserDTO(m.username, m.age) FROM Member m
     - 패키지 명을 포함한 전체 클래스 명 입력
     - 순서와 타입이 일치하는 생성자 필요

### 페이징 API 
- JPA는 페이징을 다음 두 API로 추상화
  - **setFirstResult**(int startPosition) : 조회 시작 위치 (0부터 시작)
  - **setMaxResults**(int maxResult) : 조회할 데이터 수
####
---