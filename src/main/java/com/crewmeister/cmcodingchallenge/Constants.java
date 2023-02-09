package com.crewmeister.cmcodingchallenge;


/**
 * {@link Constants} file contains all the project specific constants.
 */
public final class Constants {
    public static final String CURRENCY_SERVICE = "currencyService";

    public static final String EXCHANGE_API_BASE_URL = "https://api.apilayer.com/exchangerates_data";

    public static final String EXCHANGE_CURRENCIES_SYMBOLS_API = EXCHANGE_API_BASE_URL + "/symbols";
    public static final String EXCHANGE_CONVERT_API = EXCHANGE_API_BASE_URL + "/convert?to=%s&from=%s&amount=%s";
    public static final String EXCHANGE_RATES_API = Constants.EXCHANGE_API_BASE_URL + "/%s?symbols=%s&base=%s";


}
