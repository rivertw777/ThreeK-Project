package com.ThreeK_Project.api_server.domain.payment.enums.converter;

import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import jakarta.persistence.AttributeConverter;

public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
    @Override
    public String convertToDatabaseColumn(PaymentStatus paymentStatus) {
        if(paymentStatus == null)
            return null;
        return paymentStatus.getCodeValue();
    }

    @Override
    public PaymentStatus convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        return PaymentStatus.getEnum(dbData);
    }
}
