package com.ThreeK_Project.api_server.domain.payment.enums.converter;

import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import jakarta.persistence.AttributeConverter;

public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {
    @Override
    public String convertToDatabaseColumn(PaymentMethod paymentMethod) {
        if(paymentMethod == null)
            return null;
        return paymentMethod.getCodeValue();
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        return PaymentMethod.getEnum(dbData);
    }
}
