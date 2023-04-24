package com.ttt.capstone.controller;


import com.ttt.capstone.request.Login;
import com.ttt.capstone.request.Signup;
import com.ttt.capstone.response.SessionResponse;
import com.ttt.capstone.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        String accessToken = authService.signin(login);

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();

        return new SessionResponse(jws);
    }


    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        authService.signup(signup);
    }
}
    /* Token 방식
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login){
        String accessToken = authService.signin(login);
//        return new SessionResponse(accessToken);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")    // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))   // 한 달
                .sameSite("Strict")
                .build();
        log.info(">>>>>>>>>>>>> cookie={}", cookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

     */