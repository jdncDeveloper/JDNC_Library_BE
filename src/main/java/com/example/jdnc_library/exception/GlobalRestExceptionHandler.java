package com.example.jdnc_library.exception;

import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import com.example.jdnc_library.exception.clienterror._401.UnauthorizedException;
import com.example.jdnc_library.exception.clienterror._403.ForbiddenException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<List<String>> handleValidException(MethodArgumentNotValidException ex) {
        ArrayList<String> messageList = new ArrayList<>();
        try {
            for (ObjectError message : ex.getBindingResult().getAllErrors()) {
                messageList.add(message.getDefaultMessage());
            }
        }catch (Exception e) {
            messageList.add("요청이 옳지 않습니다.");
        }

        return new ResponseError<List<String>>(messageList);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<String> handleBadRequest(BadRequestException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError<String> handleUnAuthorized(UnauthorizedException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError<String> handleForbidden(ForbiddenException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError<String> handleAuthentication(AuthenticationException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError<String> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseError<String> (ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError<String> handleException(Exception ex) {
        return new ResponseError<String> (ex.getMessage());
    }
}
