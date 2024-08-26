package com.ThreeK_Project.api_server.domain.order.enums.converter;

import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;

public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        if(orderStatus == null)
            return null;
        return orderStatus.getCodeValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        return OrderStatus.getEnum(dbData);
    }

}
