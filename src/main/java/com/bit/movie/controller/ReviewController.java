package com.bit.movie.controller;

import com.bit.movie.model.MovieDTO;
import com.bit.movie.model.ReviewDTO;
import com.bit.movie.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/review/")
public class ReviewController {
    @Autowired
    private ReviewService REVIEW_SERVICE;

    @GetMapping("{id}")
    public Object showAll(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", REVIEW_SERVICE.selectAll(id));
        resultMap.put("result", "success");
        return resultMap;
    }
    @GetMapping("/detail/{id}")
    public Object showOne(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();
        ReviewDTO reviewDTO = REVIEW_SERVICE.selectOne(id);
        if (!id.matches("^\\d+$") || reviewDTO == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "유효하지 않은 글 번호입니다");
            return resultMap;
        } else {
            resultMap.put("item",reviewDTO);
            resultMap.put("result", "success");
        }
        return resultMap;
    }
    @PostMapping("write/{movieId}")
    public Object write(@PathVariable String movieId, @RequestBody ReviewDTO reviewDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        reviewDTO.setMovieId(movieId);
        if(!REVIEW_SERVICE.validateReview(reviewDTO.getWriterId(),movieId)){
            resultMap.put("result", "fail");
            resultMap.put("message", "리뷰를 이미 작성하였습니다");
            return resultMap;
        }
        try{
        REVIEW_SERVICE.insert(reviewDTO);
        resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    @PostMapping("/update")
    public Object update(@RequestBody ReviewDTO reviewDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            REVIEW_SERVICE.update(reviewDTO);
            resultMap.put("result", "success");
            resultMap.put("movieDTO", reviewDTO);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            REVIEW_SERVICE.delete(id);
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "글 삭제를 실패했습니다");
        }

        return resultMap;
    }
}

