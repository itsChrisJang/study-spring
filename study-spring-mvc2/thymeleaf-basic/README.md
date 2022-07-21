# 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술
> 1회독 완료.(~2022-07-21)
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
### link
<a href="https://bit.ly/3uIWT4L"><img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/></a>

--- 

## 요구사항 분석

### 상품 도메인 모델
- 상품 ID
- 상품명
- 가격
- 수량

### 상품 관리 기능
- 상품 목록
- 상품 상세
- 상품 등록
- 상품 수정

### 서비스 제공 흐름
![Service](src/main/resources/images/Service.png)

---
## PRG Post/Redirect/Get
### 전체 흐름
![Service](src/main/resources/images/PRG1.png)

### POST 등록 후 새로 고침
![Service](src/main/resources/images/PRG2.png)
- 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
- 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add + 상품 데이터를 서버로 전송한다.
- 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add + 상품 데이터를 서버로 다시 전송하게 된다.
- 그래서 내용은 같고, ID만 다른 상품 데이터가 계속 쌓이게 된다.

### POST, Redirect GET
![Service](src/main/resources/images/PRG3.png)
- 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
- 새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
- 웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다. 
- 따라서 마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.
- 이후 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.

#### 해결 방법
##### BasicItemController에 추가
```java
    /**
    * PRG - Post/Redirect/Get
    */
    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }
```
- 상품 등록 처리 이후에 뷰 템플릿이 아니라 상품 상세 화면으로 리다이렉트 하도록 코드를 작성해보자.
- 이런 문제 해결 방식을 PRG Post/Redirect/Get 라 한다.

> **주의** <br/>
> "redirect:/basic/items/" + item.getId() redirect에서 + item.getId() 처럼 URL에 변수를
> 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험하다. 다음에 설명하는 `RedirectAttributes` 를
> 사용하자.

##### RedirectAttributes
```java
    /**
    * RedirectAttributes
    */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }
```
- `RedirectAttributes` 를 사용하면 URL 인코딩도 해주고, `pathVarible` , 쿼리 파라미터까지 처리해준다.
- `redirect:/basic/items/{itemId}`
   - pathVariable 바인딩: `{itemId}`
   - 나머지는 쿼리 파라미터로 처리: `?status=true`

---
> 출처 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
---