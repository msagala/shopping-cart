package com.factura.shoppingcart.exception.handler;

import com.factura.shoppingcart.constant.StatusEnum;
import com.factura.shoppingcart.exception.CartNotFoundException;
import com.factura.shoppingcart.exception.ItemNotFoundException;
import com.factura.shoppingcart.model.dto.ErrorDto;
import com.factura.shoppingcart.model.dto.base.BaseResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setStatus(StatusEnum.FAIL.getStatus());
        baseResponseDto.setData(new ErrorDto(ex.getMessage(), request.getDescription(false), LocalDateTime.now()));
        return ResponseEntity.internalServerError().body(baseResponseDto);
    }

    @ExceptionHandler({CartNotFoundException.class, ItemNotFoundException.class})
    public final ResponseEntity<Object> handleCartNotFoundException(Exception ex, WebRequest request) throws Exception {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setStatus(StatusEnum.FAIL.getStatus());
        baseResponseDto.setData(new ErrorDto(ex.getMessage(), request.getDescription(false), LocalDateTime.now()));
        return new ResponseEntity(baseResponseDto, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setStatus(StatusEnum.FAIL.getStatus());
        baseResponseDto.setData(new ErrorDto(errorList.toString(), request.getDescription(false), LocalDateTime.now()));
        return new ResponseEntity(baseResponseDto, HttpStatus.BAD_REQUEST);
    }
}
