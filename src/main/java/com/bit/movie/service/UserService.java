package com.bit.movie.service;

import com.bit.movie.model.UserDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SqlSession sqlSession;

    private final String NAMESPACE = "mappers.UserMapper";

    public UserDTO loadByUsername(String username){
        return sqlSession.selectOne("selectByUsername", username);
    }
    public void register(UserDTO userDTO){
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        sqlSession.insert(NAMESPACE+".register",userDTO);
    }
    public boolean validateUsername(UserDTO userDTO){
        return sqlSession.selectOne(NAMESPACE+".validateUsername",userDTO)==null;
    }

    public void updateUserRole(int userId, String newRole) {
        if (!newRole.startsWith("ROLE_")) {
            newRole = "ROLE_" + newRole.toUpperCase();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("role", newRole);
        params.put("id", userId);
        sqlSession.update(NAMESPACE+".updateRole", params);
    }

    public boolean validateNickname(UserDTO userDTO){
        return sqlSession.selectOne(NAMESPACE+".validateNickname",userDTO)==null;
    }
}
