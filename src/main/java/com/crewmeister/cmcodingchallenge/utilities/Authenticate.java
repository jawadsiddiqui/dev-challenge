package com.crewmeister.cmcodingchallenge.utilities;

import com.crewmeister.cmcodingchallenge.Constants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public class Authenticate {

    public static final String EXCHANGE_API_KEY = "apikey";

    /**
     * Get token from this if api you are getting error 401 https://exchangeratesapi.io.
     *
     * @return HttpEntity with apiKey token
     */
    public static HttpEntity<String> addCustomAuthHeaders(Map<String, String> header) {
        HttpHeaders headers = new HttpHeaders();
        header.forEach((key, value) -> {
            //filter only allowed headers
           if(key.equalsIgnoreCase(EXCHANGE_API_KEY))
             headers.add(EXCHANGE_API_KEY, value);
        });
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        return httpEntity;
    }
}
