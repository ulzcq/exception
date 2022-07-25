package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @ControllerAdvice 또는 @RestControllerAdvice 를 사용하면 컨트롤러에서 발생한 오류들을 여기에서 처리한다
 * - 대상을 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여하는 역할
 * - 대상을 지정하지 않으면 모든 컨트롤러에 글로벌 적용된다.
 * - 대상 지정 방법은 1.특정 애노테이션 2.패키지 3.특정 컨트롤러 등이 있으니 스프링 공식 문서 참고
 */
@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api") //@ControllerAdvice + @ResponseBody
public class ExControllerAdvice {

    /** @ExceptionHandler는 이 컨트롤러 안의 예외에만 적용된다.
     *  해당 예외의 자식 예외까지 처리해준다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400 에러
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage()); //바로 정상흐름으로 바꿈-> 상태코드가 200이 되버림 -> @ResponseStatus로 바꾸기
    }

    /** 내가 직접 만든 예외 처리 */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e){
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST); //ResponseEntity는 HTTP 응답코드를 동적으로 변경 가능!
    }

    /** 그 외의 놓친 예외들은, 일반적인 개발의 최상위 예외인 Exception으로 잡는다. */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500 에러
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
