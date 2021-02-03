package com.github.thiagolocatelli.transfer.repository.jpa;

import com.github.thiagolocatelli.transfer.model.TransferStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransferStatusAttributeConverter implements AttributeConverter<TransferStatus, String> {
    @Override
    public String convertToDatabaseColumn(TransferStatus attribute) {
        return attribute.name();
    }

    @Override
    public TransferStatus convertToEntityAttribute(String dbData) {
        return TransferStatus.valueOf(dbData);
    }
}
