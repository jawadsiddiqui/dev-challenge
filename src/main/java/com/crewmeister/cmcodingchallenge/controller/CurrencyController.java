package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.currency.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    public static final String EXCHANGE_RATE_API_URL = "https://api.apilayer.com/exchangerates_data";

    private static String apiKey = "sg1VdvNUP4EPHgF2HIsUUrOIFeqop4jS";


    /**
     * Get All currencies.
     *
     * @return list of all currencies available
     */
    @GetMapping("/currencies")
    public ResponseEntity<ArrayList<CurrencyConversionRates>> getCurrencies() {

        String url = String.format(EXCHANGE_RATE_API_URL+"/symbols");

        ArrayList<String> currencies = new ArrayList<String>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", apiKey);
        HttpEntity<ExchangeRateResponse> entity = new HttpEntity<>(headers);


        ResponseEntity <ExchangeRateResponse> response = restTemplate.exchange(url,HttpMethod.GET, entity, ExchangeRateResponse.class);

        currencies.addAll(response.getBody().getRates().keySet());

        return new ResponseEntity<ArrayList<CurrencyConversionRates>>((MultiValueMap<String, String>) currencies, HttpStatus.OK);
    }

    @GetMapping("/rates")
    public ResponseEntity<ExchangeRateResponse> getRates(@RequestParam Date date, @RequestParam String base, @RequestParam String target) {

        String url = String.format(EXCHANGE_RATE_API_URL+"/%s?symbols=%s&base=%s", date , target, base);


        RestTemplate restTemplate = new RestTemplate();
        ExchangeRateResponse exchangeRateResponse = restTemplate.getForObject(url, ExchangeRateResponse.class);

        Double rate = exchangeRateResponse.getRates().get(target);
        exchangeRateResponse.setBase(base);
        exchangeRateResponse.getRates().clear();
        exchangeRateResponse.getRates().put(target, rate);

        return new ResponseEntity<ExchangeRateResponse>(exchangeRateResponse, HttpStatus.OK);
    }

    @GetMapping("/convert")
    public ResponseEntity<Double> convert(@RequestParam Double amount, @RequestParam String from, @RequestParam String to, @RequestParam String date) {
        String url = String.format(EXCHANGE_RATE_API_URL+"/convert?to=%s&from=%s&amount=%s", to, from, amount);

        RestTemplate restTemplate = new RestTemplate();
        ExchangeRateResponse exchangeRateResponse = restTemplate.getForObject(url, ExchangeRateResponse.class);

        Double rate = exchangeRateResponse.getRates().get(to);
        Double convertedAmount = amount * rate;

        return new ResponseEntity<Double>(convertedAmount, HttpStatus.OK);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    
}
