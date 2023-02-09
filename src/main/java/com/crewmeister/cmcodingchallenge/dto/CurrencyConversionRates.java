package com.crewmeister.cmcodingchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionRates {
    private String base;
    private Map<String, Double> rates;
}
