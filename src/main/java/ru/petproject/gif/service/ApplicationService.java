package ru.petproject.gif.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petproject.gif.client.ExchangeClient;
import ru.petproject.gif.client.GifClient;
import ru.petproject.gif.configuration.ApplicationConfiguration;
import ru.petproject.gif.exception.ExchangeException;
import ru.petproject.gif.exception.GifException;
import ru.petproject.gif.model.GifResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ApplicationService {
    private final ApplicationConfiguration configuration;
    private final ExchangeClient exchangeClient;
    private final GifClient gifClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApplicationService(ApplicationConfiguration configuration, ExchangeClient exchangeClient, GifClient gifClient, ObjectMapper objectMapper) {
        this.exchangeClient = exchangeClient;
        this.configuration = configuration;
        this.gifClient = gifClient;
        this.objectMapper = objectMapper;
    }

    public GifResponse getGifResponse() throws IOException, ExchangeException, GifException {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now().minusDays(1);
        String historical = "historical/" + date.format(formatters) + ".json";

        Double exchangeRubLatest = getExchangeRUB(getExchangeAndCheckException("latest.json"));
        Double exchangeRubPrevDay = getExchangeRUB(getExchangeAndCheckException(historical));

        if (exchangeRubPrevDay < exchangeRubLatest) {
            String gifRich = getGifAndCheckException("rich");
            return getGif(gifRich);
        } else {
            String gifBroke = getGifAndCheckException("broke");;
            return getGif(gifBroke);
        }
    }

    private Double getExchangeRUB(String jsonExchange) throws IOException {
        return objectMapper.readTree(jsonExchange).get("rates").get("RUB").asDouble();
    }

    private GifResponse getGif(String jsonGif) throws IOException {
        return GifResponse.builder()
                .url(objectMapper.readTree(jsonGif).get("data").get("images").get("downsized_large").get("url").asText())
                .width(objectMapper.readTree(jsonGif).get("data").get("images").get("downsized_large").get("width").asText())
                .height(objectMapper.readTree(jsonGif).get("data").get("images").get("downsized_large").get("height").asText())
                .build();

    }

    private String getExchangeAndCheckException(String api) throws ExchangeException {
        try {
            return exchangeClient.findExchangeRub(api, configuration.getExchangeApiId());
        } catch (Throwable e) {
            throw new ExchangeException("Error during request exchange value");
        }
    }

    private String getGifAndCheckException (String tag) throws GifException {
        try {
            return gifClient.findGif(configuration.getGifApiId(), tag);
        } catch (Throwable e) {
            throw new GifException("Error during request gif value for " + tag);
        }
    }


}
