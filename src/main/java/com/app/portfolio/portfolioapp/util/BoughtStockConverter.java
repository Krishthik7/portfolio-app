package com.app.portfolio.portfolioapp.util;

import com.app.portfolio.portfolioapp.entity.BoughtStock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BoughtStockConverter implements AttributeConverter<BoughtStock, String> {

    @Override
    public String convertToDatabaseColumn(BoughtStock boughtStock) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(boughtStock);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BoughtStock convertToEntityAttribute(String dbPersonName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(dbPersonName, BoughtStock.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}