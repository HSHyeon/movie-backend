package com.bit.movie.controller;

import com.bit.movie.model.ReviewDTO;
import com.bit.movie.model.UserDTO;
import com.bit.movie.provider.JwtProvider;
import com.bit.movie.service.ReviewService;
import com.bit.movie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/review/")
public class ReviewController {
    @Autowired
    private ReviewService REVIEW_SERVICE;
    @Autowired
    private UserService USER_SERVICE;
    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("{id}")
    public Object showAll(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", REVIEW_SERVICE.selectAll(id));
        resultMap.put("total", REVIEW_SERVICE.getTotal(id));
        resultMap.put("result", "success");
        return resultMap;
    }

    @GetMapping("detail/{id}")
    public Object showOne(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();
        ReviewDTO reviewDTO = REVIEW_SERVICE.selectOne(id);
        if (!id.matches("^\\d+$") || reviewDTO == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "유효하지 않은 글 번호입니다");
            return resultMap;
        } else {
            resultMap.put("item", reviewDTO);
            resultMap.put("result", "success");
        }
        return resultMap;
    }

    @PostMapping("write/score")
    public Object write(@RequestBody ReviewDTO reviewDTO, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7);
            String username = jwtProvider.getUsername(token);
            UserDTO userDTO = USER_SERVICE.loadByUsername(username);
            reviewDTO.setWriterId(userDTO.getId());

            if (!REVIEW_SERVICE.validateReview(userDTO.getId(), reviewDTO.getMovieId())) {
                resultMap.put("result", "fail");
                resultMap.put("message", "리뷰를 이미 작성하였습니다");
                return resultMap;
            }
            REVIEW_SERVICE.insert(reviewDTO);
            resultMap.put("reviewId", reviewDTO.getId());
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @PostMapping("write/content")
    public Object write(@RequestBody ReviewDTO reviewDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            REVIEW_SERVICE.writeContent(reviewDTO.getId(), reviewDTO.getContent());
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @PostMapping("update")
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

    @GetMapping("delete/{id}")
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

