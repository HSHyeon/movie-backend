package com.bit.movie.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {
    private int id;
    private String content;
    private String writerId;
    private String movieId;
    private int score;
    private String nickname;
    private Date entryDate;
    private Date modifyDate;
}
