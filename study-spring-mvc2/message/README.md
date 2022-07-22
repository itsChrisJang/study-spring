# 스프링 MVC 2편 - 백엔드 웹 개발 핵심 기술
## 메시지, 국제화
--- 
### Commit Role
<details>
  <summary>
    <code>Commit Type</code> 
  </summary>

##### type

- feat : 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
- fix : 기능에 대한 버그 수정
- build : 빌드 관련 수정
- chore : 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore
- ci : CI 관련 설정 수정
- docs : 문서(주석) 수정
- style : 코드 스타일, 포맷팅에 대한 수정
- refactor : 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경
- test : 테스트 코드 추가/수정
- release : 버전 릴리즈
</details>

---
### 메시지
- 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능이라 한다.
- 예를 들어서 messages.properties 라는 메시지 관리용 파일을 만들고 각 HTML들은 다음과 같이 해당 데이터를 key 값으로 불러서 사용하는 것이다.
```properties
item=상품
item.id=상품 ID
item.itemName=상품명
item.price=가격
item.quantity=수량
```

### 국제화
> 메시지에서 한 발 더 나가보자.
- 메시지에서 설명한 메시지 파일( messages.properties )을 각 나라별로 별도로 관리하면 서비스를 국제화 할 수 있다.
   1. messages_en.properties
   2. messages_ko.properties
- 한국에서 접근한 것인지 영어에서 접근한 것인지는 인식하는 방법은 HTTP accept-language 해더 값을 사용하거나 사용자가 직접 언어를 선택하도록 하고, 쿠키 등을 사용해서 처리하면 된다.

---