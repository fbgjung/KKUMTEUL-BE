package com.kkumteul.exception.handler;

import com.kkumteul.exception.*;
import com.kkumteul.exception.ChildProfileNotFoundException;
import com.kkumteul.exception.HistoryNotFoundException;
import com.kkumteul.exception.InvalidMBTINameException;
import com.kkumteul.exception.RecommendationBookNotFoundException;
import com.kkumteul.exception.UserNotFoundException;
import com.kkumteul.util.ApiUtil;
import com.kkumteul.util.ApiUtil.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.persistence.EntityNotFoundException;
import com.kkumteul.exception.RecommendationBookNotFoundException;
import com.kkumteul.util.ApiUtil;
import com.kkumteul.util.ApiUtil.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ApiError<String> error = ApiUtil.error(statusCode.value(), "알 수 없는 오류가 발생했습니다. 문의 바랍니다.");
        return super.handleExceptionInternal(ex, error, headers, statusCode, request);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            RecommendationBookNotFoundException.class,
            ChildProfileNotFoundException.class,
            EntityNotFoundException.class,
            HistoryNotFoundException.class,
            UserNotFoundException.class,
            AdminBookNotFoundException.class
    })
    protected ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        log.error(e.getMessage(), e);
        ApiError<String> error = ApiUtil.error(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(error);
    }

    @ExceptionHandler({
            InvalidMBTINameException.class
    })
    protected ResponseEntity<?> handleInvalidException(Exception e) {
        log.error(e.getMessage(), e);
        ApiError<String> error = ApiUtil.error(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TokenExpiredException.class)
    protected ResponseEntity<?> handleExpiredJwtException(TokenExpiredException e) {
        log.error(e.getMessage(), e);
        ApiError<String> error = ApiUtil.error(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
