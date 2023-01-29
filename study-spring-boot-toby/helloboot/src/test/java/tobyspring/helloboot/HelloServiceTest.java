package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloServiceTest {

    @Test
    void simpleHelloService() {
        SimpleHelloService helloService = new SimpleHelloService();

        String ret = helloService.sayHello("Test");

        Assertions.assertThat(ret).isEqualTo("Hello Test");
    }
    
    @Test
    public void helloDecorator() throws Exception {
        //given
        HelloDecorator decorator = new HelloDecorator(name -> name);

        //when
        String ret = decorator.sayHello("Test");

        //then
        Assertions.assertThat(ret).isEqualTo("*Test*");
    }
}
