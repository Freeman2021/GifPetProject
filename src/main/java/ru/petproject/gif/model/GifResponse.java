package ru.petproject.gif.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GifResponse {
    private String url;
    private String width;
    private String height;
}
