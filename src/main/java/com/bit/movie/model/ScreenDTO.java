package com.bit.movie.model;

import lombok.Data;

@Data
public class ScreenDTO {
    private String id;
    private String theaterId;
    private String movieId;
    private String time;
}
