package com.crewmeister.cmcodingchallenge.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionRates {
    private String conversionBase;
    private double conversionRate;

}
