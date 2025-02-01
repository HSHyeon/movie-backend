package com.bit.movie.controller;

import com.bit.movie.model.TokenDTO;
import com.bit.movie.model.UserDTO;
import com.bit.movie.service.AuthService;
import com.bit.movie.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/")
@AllArgsConstructor
public class UserController {
    private final UserService USER_SERVICE;
    @Autowired
    private AuthService authService;

    @PostMapping("auth")
    public Object auth(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        TokenDTO tokenDTO = authService.auth(userDTO);
        UserDTO getUser = USER_SERVICE.loadByUsername(userDTO.getUsername());
        if (tokenDTO != null) {
            Cookie jwtCookie = new Cookie("jwt", tokenDTO.getValue());
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600);
            response.addCookie(jwtCookie);
            resultMap.put("result", "success");
            resultMap.put("authority", getUser.getRole());
            resultMap.put("id", getUser.getId());
            resultMap.put("message", "로그인 성공");
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "로그인 정보를 다시 확인하세요");
        }
        return resultMap;
    }

    @GetMapping("logout")
    public Object logout(HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();

        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        resultMap.put("result", "success");
        resultMap.put("message", "로그아웃 성공");
        return resultMap;
    }

    @PostMapping("register")
    public Object register(@RequestBody UserDTO userDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        if (!USER_SERVICE.validateUsername(userDTO)) {
            resultMap.put("result", "fail");
            resultMap.put("message", "중복된 아이디입니다.");
        } else if (!USER_SERVICE.validateNickname(userDTO)) {
            resultMap.put("result", "fail");
            resultMap.put("message", "중복된 닉네임입니다.");
        } else {
            USER_SERVICE.register(userDTO);
            resultMap.put("result", "success");
        }
        return resultMap;
    }

    /*
    * 사용자가 임의로 자신의 권한을 수정할 수 있음
    * TODO 관리자가 권한을 허용하면 바뀔 수 있도록 수정
    * */
    @PostMapping("role")
    public Object update(@RequestParam String newRole, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String username = userDetails.getUsername();
            int userId = USER_SERVICE.loadByUsername(username).getId();
            USER_SERVICE.updateUserRole(userId, newRole);
            resultMap.put("result", "success");
            resultMap.put("authority", newRole);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
