package com.bit.movie.model;

import lombok.Data;

import java.util.Date;

@Data
public class MovieDTO {
    private int id;
    private String title;
    private String content;
    private String genre;
    private String imageUrl;
    private String releaseDate;
    private int writerId;
    private String nickname;
    private Date entryDate;
    private Date modifyDate;
    private float averageRating;
}
