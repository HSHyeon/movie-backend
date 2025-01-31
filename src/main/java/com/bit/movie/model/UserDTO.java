package com.bit.movie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements UserDetails {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String role;
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities == null){
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        // 해당 유저의 비밀번호
        return password;
    }

    @Override
    public String getUsername() {
        // 해당 유저의 유저네임
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 해당 계정이 만료가 안되었는지 판별
        // 만료기간이 남았으면(만료되지 않았으면) -> true
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨있는가?
        // 안 잠겨있으면 -> true
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 유효기간이 만료되지 않았는가?
        // 아직 유효하다 -> true
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자 계정이 사용가능한가
        // 가능하다 -> true
//        return UserDetails.super.isEnabled();
        return true;
    }
}
