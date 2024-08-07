package com.factura.shoppingcart.util;

import com.factura.shoppingcart.constant.StatusEnum;
import com.factura.shoppingcart.model.dto.base.BaseResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MapperUtil {

    private static ObjectMapper objectMapper;

    public static void setObjectMapper(ObjectMapper objectMapper) {
        MapperUtil.objectMapper = objectMapper;
    }

    public static String objectToJson( Object obj ) {
        try {
            return objectMapper.writeValueAsString( obj );
        } catch (JsonProcessingException var2) {
            log.debug( var2.getMessage(), var2 );
            log.error( "e: ", var2 );
        } catch (Exception var3) {
            log.debug( var3.getMessage(), var3 );
            log.error( "e: ", var3 );
        }
        return null;
    }

    public static <T> BaseResponseDto<T> getSuccessBaseResponse(T body) {
        BaseResponseDto<T> cartDtoBaseResponseDto = new BaseResponseDto<>();
        cartDtoBaseResponseDto.setStatus(StatusEnum.SUCCESS.getStatus());
        cartDtoBaseResponseDto.setData(body);
        return cartDtoBaseResponseDto;
    }

}
