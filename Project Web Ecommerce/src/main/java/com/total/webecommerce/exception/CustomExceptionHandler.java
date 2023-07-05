package com.total.webecommerce.exception;

import com.total.webecommerce.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(BadResquestException.class)
    public ResponseEntity<?> handlenBadresquestException(BadResquestException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    // xử lí custom trả về cho exception Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlenNotFoundException(NotFoundException e){
        ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlenOtherException(Exception e){
        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlenExceptionValidate(MethodArgumentNotValidException ex){
        Map<String , String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (err)->{
                    String filedName = ((FieldError) err).getField();
                    String errorName = err.getDefaultMessage();
                    errors.put(filedName,errorName);
                }
        );
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,errors);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }



}
