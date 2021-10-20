package ru.petproject.gif.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="exchange", url="https://openexchangerates.org")
public interface ExchangeClient {

    @GetMapping(value="/api/{api}?app_id={api_id}&base=USD&symbols=RUB",consumes= MediaType.APPLICATION_JSON_VALUE)
    String findExchangeRub(@PathVariable("api") String exchangeApi, @PathVariable("api_id")  String apiId);
}
