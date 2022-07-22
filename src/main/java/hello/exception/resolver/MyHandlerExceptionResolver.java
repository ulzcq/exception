package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST); //예외 상태코드를 400으로 바꾸고
                return new ModelAndView(); //빈 ModelAndView를 반환 -> WAS한테 정상응답으로 돌아간 후 sendError를 발견
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null; //null을 반환하면 다음 ExceptionResolver를 찾아서 실행, 없으면 다시 예외를 서블릿 밖으로 던짐 -> WAS 500
    }
}
