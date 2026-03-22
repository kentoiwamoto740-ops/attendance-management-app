package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.entity.User;
import com.iwamoto.attendance.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;

    }

    //登録画面表示
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    //登録処理
    @PostMapping("/register")
    public  String register(@RequestParam String username,
                            @RequestParam String email,
                            @RequestParam String password,
                            Model model){

        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            userService.register(user);

            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
