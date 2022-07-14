package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("username={}, age={}", memberName, memberAge);
        return"ok";
    }

    @ResponseBody
    @RequestMapping("request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return"ok";
    }

    @ResponseBody
    @RequestMapping("request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return"ok";
    }

    /*
    주의! - 기본형(primitive)에 null 입력
    - /request-param 요청
    - @RequestParam(required = false) int age

    null 을 int 에 입력하는 것은 불가능(500 예외 발생)
    따라서 null 을 받을 수 있는 Integer 로 변경하거나, 또는 다음에 나오는 defaultValue 사용
     */
    @ResponseBody
    @RequestMapping("request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true)  String username,
//            @RequestParam(required = false) int age) {
            @RequestParam(required = false) Integer age) {


        log.info("username={}, age={}", username, age);
        return"ok";
    }

    @ResponseBody
    @RequestMapping("request-param-default")
    public String requestParamDefault(
            @RequestParam(defaultValue = "guest")  String username,
            @RequestParam(defaultValue = "-1") Integer age) {


        log.info("username={}, age={}", username, age);
        return"ok";
    }

    /*
    @RequestParam Map ,
        - Map(key=value)
    @RequestParam MultiValueMap
        - MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1, id2])
     */
    @ResponseBody
    @RequestMapping("request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return"ok";
    }
}
