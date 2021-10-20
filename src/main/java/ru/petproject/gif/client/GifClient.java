package ru.petproject.gif.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="gif", url="https://api.giphy.com")
public interface GifClient {

    @GetMapping(value="/v1/gifs/random?api_key={api_id}&tag={tag}",consumes= MediaType.APPLICATION_JSON_VALUE)
    String findGif(@PathVariable("api_id") String apiId, @PathVariable("tag")  String tag);
}
