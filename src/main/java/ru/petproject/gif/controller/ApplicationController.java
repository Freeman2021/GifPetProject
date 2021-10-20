package ru.petproject.gif.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.petproject.gif.exception.ExchangeException;
import ru.petproject.gif.exception.GifException;
import ru.petproject.gif.model.GifResponse;
import ru.petproject.gif.service.ApplicationService;

import java.io.IOException;

@RestController
public class ApplicationController {
    private final ApplicationService service;

    @Autowired
    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping(path = "app")
    public ResponseEntity<?> getGif() throws IOException {
        try {
            GifResponse response = service.getGifResponse();
            return ResponseEntity.status(200)
                    .body(String.format("<img src=\"%s\" width=\"%s\" height=\"%s\"/>",
                            response.getUrl(), response.getWidth(), response.getHeight())
                    );
        } catch (ExchangeException | GifException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
