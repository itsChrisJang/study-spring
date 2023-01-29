package tobyspring.helloboot;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class HelloDecorator implements HelloService{

    private final  HelloService helloService;

    public HelloDecorator(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public String sayHello(String name) {
        return "*" + helloService.sayHello(name) + "*";
    }
}
