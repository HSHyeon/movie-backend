package com.bit.movie.controller;

import com.bit.movie.model.MovieDTO;
import com.bit.movie.model.ReviewDTO;
import com.bit.movie.provider.JwtProvider;
import com.bit.movie.service.MovieService;
import com.bit.movie.service.ReviewService;
import com.bit.movie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/movie")
@AllArgsConstructor
public class MovieController {
    @Autowired
    private ReviewService REVIEW_SERVICE;
    @Autowired
    private MovieService MOVIE_SERVICE;
    @Autowired
    private UserService USER_SERVICE;
    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("")
    public Object showAll(@RequestParam(defaultValue = "1") int page) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", MOVIE_SERVICE.selectAll(page));
        resultMap.put("total",MOVIE_SERVICE.selectMaxPage());
        resultMap.put("result", "success");
        return resultMap;
    }

    @GetMapping("/detail/{id}")
    public Object showOne(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();
        MovieDTO movieDTO = MOVIE_SERVICE.selectOne(id);
        List<ReviewDTO> latestReviews = REVIEW_SERVICE.selectReviewsByMovieId(id, 3);

        if (!id.matches("^\\d+$") || movieDTO == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "유효하지 않은 글 번호입니다");
            return resultMap;
        } else {
            resultMap.put("item", movieDTO);
            resultMap.put("reviewTotal",REVIEW_SERVICE.getTotal(id));
            resultMap.put("review", latestReviews);
            resultMap.put("result", "success");
        }
        return resultMap;
    }

    @PostMapping("/write")
    public Object write(@RequestBody MovieDTO movieDTO, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7);
            String username = jwtProvider.getUsername(token);

            int userId = USER_SERVICE.loadByUsername(username).getId();
            movieDTO.setWriterId(userId);
            MOVIE_SERVICE.insert(movieDTO);
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @PostMapping("/update")
    public Object update(@RequestBody MovieDTO movieDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MOVIE_SERVICE.update(movieDTO);
            resultMap.put("result", "success");
            resultMap.put("movieDTO", movieDTO);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /*
     * 관리자는 모든 영화를 삭제할 수 있다.
     * */
    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            MOVIE_SERVICE.delete(id);
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "글 삭제를 실패했습니다");
        }

        return resultMap;
    }
}
