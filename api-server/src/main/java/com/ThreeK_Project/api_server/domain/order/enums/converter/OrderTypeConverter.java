package com.ThreeK_Project.api_server.domain.order.enums.converter;

import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import jakarta.persistence.AttributeConverter;

public class OrderTypeConverter implements AttributeConverter<OrderType, String> {
    @Override
    public String convertToDatabaseColumn(OrderType orderType) {
        if(orderType == null)
            return null;
        return orderType.getCodeValue();
    }

    @Override
    public OrderType convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        return OrderType.getEnum(dbData);
    }
}
