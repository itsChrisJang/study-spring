package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {
    
    @Test
    public void helloController() throws Exception {
        //given
        HelloController helloController = new HelloController((HelloService) name -> name);

        //when
        String ret = helloController.hello("Test");

        //then
        Assertions.assertThat(ret).isEqualTo("Test");
    }
    
    @Test
    public void failHelloController() throws Exception {
        //given
        HelloController helloController = new HelloController((HelloService) name -> name);
        
        //when

        //then
        Assertions.assertThatThrownBy(() -> {
            helloController.hello(null);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            helloController.hello("");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
