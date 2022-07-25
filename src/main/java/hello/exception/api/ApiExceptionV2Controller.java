package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

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

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값"); //id값이 bad여서는 안된다고 정의했다고 가정
        }
        if (id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
