package com.example.search.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SearchException.class)
    public ResponseEntity<ErrorResponse> handleSearchException(
            SearchException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI().toString(),
                List.of()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
//    @ExceptionHandler(InvalidQueryException.class)
//    public ResponseEntity<ErrorResponse>handleInvalidQuery(InvalidQueryException ex,HttpServletRequest request){
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.getReasonPhrase(),
//                ex.getMessage(),
//                request.getRequestURI().toString(),
//                List.of()
//        );
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(error);
//    }
}
