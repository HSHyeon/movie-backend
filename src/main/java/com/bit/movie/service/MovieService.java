package com.bit.movie.service;

import com.bit.movie.model.MovieDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private SqlSession sqlSession;

    private final String NAMESPACE = "mappers.MovieMapper";

    public List<MovieDTO> selectAll() {
        return sqlSession.selectList(NAMESPACE + ".selectAll");
    }

    public MovieDTO selectOne(String id) {
        return sqlSession.selectOne(NAMESPACE + ".selectOne",id);
    }

    public void insert(MovieDTO fileDTO) {
        sqlSession.insert(NAMESPACE + ".insert", fileDTO);
    }
    // 글 수정하기
    public void update(MovieDTO movieDTO) {
        sqlSession.update(NAMESPACE + ".update", movieDTO);
    }

    // 글 삭제하기
    public void delete(String id) {
        sqlSession.delete(NAMESPACE + ".delete", id);
    }
}
