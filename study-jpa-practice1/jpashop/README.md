## 실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발
- 핵심 라이브러리 
  - 스프링 MVC 
  - 스프링 ORM
  - JPA, 하이버네이트
  - 스프링 데이터 JPA 
- 기타 라이브러리
  - H2 데이터베이스 클라이언트 
  - 커넥션 풀: 부트 기본은 HikariCP 
  - WEB(thymeleaf)
  - 로깅 SLF4J & LogBack
  - 테스트

### 기능 목록
![img](./readmeIMG/sample.png)
- 회원 기능
  - 회원 등록
  - 회원 조회
- 상품 기능
  - 상품 등록
  - 상품 수정
  - 상품 조회
- 주문 기능 
  - 상품 주문
  - 주문 내역 조회
  - 주문 취소
- 기타 요구사항
  - 상품은 재고 관리가 필요하다.
  - 상품의 종류는 도서, 음반, 영화가 있다. 상품을 카테고리로 구분할 수 있다.
  - 상품 주문시 배송 정보를 입력할 수 있다.

## 도메인 모델과 테이블 설계
![img](./readmeIMG/domainModel.png)
### 회원 엔티티 분석
![img](./readmeIMG/memberEntity.png)
### 회원 테이블 분석
![img](./readmeIMG/memberTable.png)
