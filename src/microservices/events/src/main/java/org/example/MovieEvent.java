package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieEvent {
    private Integer movieId;
    private String title;
    private String action;
    private Integer userId;
    private Float rating;
    private List<String> genres;
    private String description;
}