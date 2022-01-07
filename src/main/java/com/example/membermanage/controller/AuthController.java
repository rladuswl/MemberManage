package com.example.membermanage.controller;

import com.example.membermanage.entity.User;
import com.example.membermanage.service.AuthService;
import com.example.membermanage.web.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signin")
    public String SigninForm() {
        return "signin";
    }

    @GetMapping("/signup")
    public String SignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(User user) {

        User newUser = user; //새로운 유저 받음

        User userEntity = authService.signup(user);
        System.out.println(userEntity);

        return "signin";
    }
}
