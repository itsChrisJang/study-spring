package hello.springmvc.basic.requestMapping;

import lombok.Data;

//@Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 를 자동으로 적용
@Data
public class HelloData {
    private String username;
    private int age;
}
