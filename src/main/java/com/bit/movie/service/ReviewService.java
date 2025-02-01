package com.bit.movie.service;

import com.bit.movie.model.ReviewDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    @Autowired
    private SqlSession sqlSession;
    private final String NAMESPACE = "mappers.ReviewMapper";

    public List<ReviewDTO> selectAll(String id){
        return sqlSession.selectList(NAMESPACE+".selectAll",id);
    }
    public List<ReviewDTO> selectReviewsByMovieId(String movieId, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("movieId", movieId);
        params.put("limit", limit);
        return sqlSession.selectList(NAMESPACE+".findLatestReviewsByMovieId", params);
    }
    public boolean validateReview(int id, int movieId){
        Map<String, Object> params = new HashMap<>();
        params.put("movieId", movieId);
        params.put("id", id);
        return sqlSession.selectOne("selectById", params)==null;
    }
    public ReviewDTO selectOne(String id){
        return sqlSession.selectOne(NAMESPACE+".selectOne",id);
    }
    public void insert(ReviewDTO reviewDTO){
        sqlSession.insert(NAMESPACE+".insert",reviewDTO);
    }
    public void delete(String id){
        sqlSession.delete(NAMESPACE+".delete",id);
    }
    public void update(ReviewDTO reviewDTO) {
        sqlSession.update(NAMESPACE + ".update", reviewDTO);
    }
    public int getMovieIdByReplyId(int reviewId) {
        return sqlSession.selectOne(NAMESPACE + ".getMovieIdByReplyId", reviewId);
    }
}
