package ru.petproject.gif;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;
import ru.petproject.gif.client.ExchangeClient;
import ru.petproject.gif.client.GifClient;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
class GifApplicationTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private ExchangeClient exchangeClient;
    @MockBean
    private GifClient gifClient;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void test1() throws Exception {
        String urlBroke = "https://media2.giphy.com/media/Ypx1EOgCqS7LhMrXgZ/giphy.gif?cid=9afb1a7399ec924cdde2296d5403f34effa51bf1f380a774&rid=giphy.gif&ct=g";
        String heightBroke = "200";
        String widthBroke = "480";
        String urlRich = "https://media0.giphy.com/media/cta8UUiFFkkzqhzihw/giphy.gif?cid=9afb1a73c3ca9f278394b4b62b96e9a93be88cf7ca80291b&rid=giphy.gif&ct=g";
        String heightRich = "270";
        String widthRich = "480";

        when(exchangeClient.findExchangeRub(eq("latest.json"), any())).thenReturn("{\"rates\":{\"RUB\":71.4065}}");
        when(exchangeClient.findExchangeRub(startsWith("historical/"), any())).thenReturn("{\"rates\":{\"RUB\":70.4065}}");
        when(gifClient.findGif(any(), eq("rich"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"864641\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightRich, urlRich, widthRich));
        when(gifClient.findGif(any(), eq("broke"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"2508860\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightBroke, urlBroke, widthBroke));

        MvcResult result = this.mockMvc.perform(get("/task"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Assert.hasText(String.format("<img src=\"%s\" width=\"%s\" height=\"%s\"/>", heightRich, urlRich, widthRich), contentAsString);
    }

    @Test
    void test2() throws Exception {
        String urlBroke = "https://media2.giphy.com/media/Ypx1EOgCqS7LhMrXgZ/giphy.gif?cid=9afb1a7399ec924cdde2296d5403f34effa51bf1f380a774&rid=giphy.gif&ct=g";
        String heightBroke = "200";
        String widthBroke = "480";
        String urlRich = "https://media0.giphy.com/media/cta8UUiFFkkzqhzihw/giphy.gif?cid=9afb1a73c3ca9f278394b4b62b96e9a93be88cf7ca80291b&rid=giphy.gif&ct=g";
        String heightRich = "270";
        String widthRich = "480";

        when(exchangeClient.findExchangeRub(eq("latest.json"), any())).thenReturn("{\"rates\":{\"RUB\":70.4065}}");
        when(exchangeClient.findExchangeRub(startsWith("historical/"), any())).thenReturn("{\"rates\":{\"RUB\":71.4065}}");
        when(gifClient.findGif(any(), eq("rich"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"864641\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightRich, urlRich, widthRich));
        when(gifClient.findGif(any(), eq("broke"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"2508860\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightBroke, urlBroke, widthBroke));

        MvcResult result = this.mockMvc.perform(get("/task"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Assert.hasText(String.format("<img src=\"%s\" width=\"%s\" height=\"%s\"/>", heightBroke, urlBroke, widthBroke), contentAsString);
    }

    @Test
    void test3() throws Exception {
        String urlBroke = "https://media2.giphy.com/media/Ypx1EOgCqS7LhMrXgZ/giphy.gif?cid=9afb1a7399ec924cdde2296d5403f34effa51bf1f380a774&rid=giphy.gif&ct=g";
        String heightBroke = "200";
        String widthBroke = "480";
        String urlRich = "https://media0.giphy.com/media/cta8UUiFFkkzqhzihw/giphy.gif?cid=9afb1a73c3ca9f278394b4b62b96e9a93be88cf7ca80291b&rid=giphy.gif&ct=g";
        String heightRich = "270";
        String widthRich = "480";

        when(exchangeClient.findExchangeRub(eq("latest.json"), any())).thenThrow(new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        when(exchangeClient.findExchangeRub(startsWith("historical/"), any())).thenReturn("{\"rates\":{\"RUB\":71.4065}}");
        when(gifClient.findGif(any(), eq("rich"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"864641\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightRich, urlRich, widthRich));
        when(gifClient.findGif(any(), eq("broke"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"2508860\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightBroke, urlBroke, widthBroke));

        this.mockMvc.perform(get("/task"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Error during request exchange value")));

    }

    @Test
    void test4() throws Exception {
        String urlRich = "https://media0.giphy.com/media/cta8UUiFFkkzqhzihw/giphy.gif?cid=9afb1a73c3ca9f278394b4b62b96e9a93be88cf7ca80291b&rid=giphy.gif&ct=g";
        String heightRich = "270";
        String widthRich = "480";

        when(exchangeClient.findExchangeRub(eq("latest.json"), any())).thenReturn("{\"rates\":{\"RUB\":70.4065}}");
        when(exchangeClient.findExchangeRub(startsWith("historical/"), any())).thenReturn("{\"rates\":{\"RUB\":71.4065}}");
        when(gifClient.findGif(any(), eq("rich"))).thenReturn(String.format("{\"data\":{\"images\":{\"downsized_large\":{\"height\":\"%s\",\"size\":\"864641\",\"url\":\"%s\",\"width\":\"%s\"}}}}", heightRich, urlRich, widthRich));
        when(gifClient.findGif(any(), eq("broke"))).thenThrow(new HttpServerErrorException(HttpStatus.BAD_REQUEST));

        this.mockMvc.perform(get("/task"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Error during request gif value for broke")));
    }

}
