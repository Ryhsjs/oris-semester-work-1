package ru.itis.flavorful_book.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    public Object handleForbidden(ForbiddenException ex,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        log.warn("Forbidden: {}", ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());

        return new ModelAndView("errors/403");
    }

    @ExceptionHandler(ConflictException.class)
    public Object handleConflict(ConflictException ex,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        log.warn("Conflict: {}", ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ex.getMessage()));
        }
        response.setStatus(HttpStatus.CONFLICT.value());
        return new ModelAndView("errors/409");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleEntityNotFound(EntityNotFoundException ex,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        log.error("Entity not found: {}", ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ModelAndView("errors/404");
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneric(Exception ex,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Внутренняя ошибка сервера"));
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ModelAndView("errors/500");
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        return accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
