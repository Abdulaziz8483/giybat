package dasturlashuz.giybat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> exception(Exception e) {
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> exception(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
