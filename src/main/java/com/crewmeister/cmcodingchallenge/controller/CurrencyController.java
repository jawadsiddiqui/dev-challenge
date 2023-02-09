package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.Constants;
import com.crewmeister.cmcodingchallenge.dto.CurrencyConversionRates;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.crewmeister.cmcodingchallenge.utilities.Authenticate.addCustomAuthHeaders;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    /**
     * Get All currencies.
     *
     * @return list of all currencies available
     */
    @GetMapping("/currencies")
    @Async
    @Retry(name = Constants.CURRENCY_SERVICE)
    public ResponseEntity<String> getCurrencies(@RequestHeader Map<String, String> header) {

        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<String> response = (restTemplate.exchange(Constants.EXCHANGE_CURRENCIES_SYMBOLS_API,
                HttpMethod.GET,
                addCustomAuthHeaders(header),
                String.class));

        return  response;
    }


    /**
     * Get Historical rates.
     *
     * @param date   Provide date e.g. YYYY-MM-DD
     * @param base   Provide base currency code e.g. EUR
     * @param target Provide target currency code e.g. USD
     * @return return @ExchangeRateResponse as body
     */
    @GetMapping("/rates")
    @Async
    @Retry(name = Constants.CURRENCY_SERVICE, fallbackMethod = "fallbackResponse")
    public ResponseEntity<CurrencyConversionRates> getRates(@RequestHeader Map<String, String> header,
                                                            @RequestParam String date,
                                                            @RequestParam String base,
                                                            @RequestParam String target) {

        String url = String.format(Constants.EXCHANGE_RATES_API, date, target, base);
        RestTemplate restTemplate = new RestTemplate();

        CurrencyConversionRates currencyConversionRates
                = restTemplate.exchange(url,
                HttpMethod.GET,
                addCustomAuthHeaders(header),
                CurrencyConversionRates.class).getBody();

        return new ResponseEntity<CurrencyConversionRates>(currencyConversionRates, HttpStatus.OK);
    }

    /**
     * Conversion from one currency to others.
     *
     * @param amount Provide date e.g. YYYY-MM-DD
     * @param from   Provide base currency code e.g. EUR
     * @param to     Provide target currency code e.g. USD
     * @return return @ExchangeRateResponse as body
     */
    @GetMapping("/convert")
    @Async
    @Retry(name = Constants.CURRENCY_SERVICE, fallbackMethod = "fallbackResponse")
    public ResponseEntity<CurrencyConversionRates> convert(@RequestHeader Map<String, String> header,
                                                           @RequestParam Double amount,
                                                           @RequestParam String from,
                                                           @RequestParam String to) {

        String url = String.format(Constants.EXCHANGE_CONVERT_API, to, from, amount);
        RestTemplate restTemplate = new RestTemplate();

        CurrencyConversionRates currencyConversionRates = new CurrencyConversionRates();

        currencyConversionRates.setBase(from);

        Map<String, Double> map = new HashMap<>();
        map.put(to, (Double) restTemplate
                .exchange(url, HttpMethod.GET, addCustomAuthHeaders(header), Map.class)
                .getBody()
                .get("result"));

        currencyConversionRates.setRates(map);

        return new ResponseEntity<CurrencyConversionRates>(currencyConversionRates, HttpStatus.OK);
    }

    public ResponseEntity<CurrencyConversionRates> fallbackResponse(Exception e) {
        CurrencyConversionRates currencyConversionRates = new CurrencyConversionRates();
        return new ResponseEntity<>(currencyConversionRates, HttpStatus.BAD_REQUEST);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
