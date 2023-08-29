package com.example.jdnc_library.feature.naverAPI.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverAPIDTO {
    @JsonProperty("lastBuildDate")
    private String lastBuildDate;

    @JsonProperty("total")
    private int total;

    @JsonProperty("start")
    private int start;

    @JsonProperty("display")
    private int display;

    @JsonProperty("items")
    private List<BookDTO> items;

    @Getter
    @Setter
    public static class BookDTO {
        @JsonProperty("title")
        private String title;

        @JsonProperty("link")
        private String link;

        @JsonProperty("image")
        private String image;

        @JsonProperty("author")
        private String author;

        @JsonProperty("discount")
        private String discount;

        @JsonProperty("publisher")
        private String publisher;

        @JsonProperty("pubdate")
        private String pubdate;

        @JsonProperty("isbn")
        private String isbn;

        @JsonProperty("description")
        private String description;
    }
}
