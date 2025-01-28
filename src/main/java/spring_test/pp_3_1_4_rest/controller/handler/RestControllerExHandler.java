package spring_test.pp_3_1_4_rest.controller.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "spring_test.pp_3_1_4_rest.controller.rest")
public class RestControllerExHandler extends ResponseEntityExceptionHandler {
}
