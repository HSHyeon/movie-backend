package com.bit.movie.service;

import com.bit.movie.model.TokenDTO;
import com.bit.movie.model.UserDTO;
import com.bit.movie.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    public TokenDTO auth(UserDTO userDTO){
        UserDTO origin = (UserDTO) userDetailsService.loadUserByUsername(userDTO.getUsername());
        if(origin == null){
            throw new UsernameNotFoundException("로그인 정보를 다시 확인해주세요");
        }
        if(!passwordEncoder.matches(userDTO.getPassword(),origin.getPassword())){
            throw new BadCredentialsException("로그인 정보를 다시 확인해주세요");
        };
        return jwtProvider.generate(origin);
    }
}
