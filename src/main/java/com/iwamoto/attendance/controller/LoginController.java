package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.entity.User;
import com.iwamoto.attendance.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    //ログイン画面表示
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    //ログイン処理
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model){

        User user = userService.login(username, password);

        //ログイン失敗
        if(user == null){
            model.addAttribute("error", "ユーザー名またはパスワードが違います");
            return "login";
        }

        //セッション保存
        session.setAttribute("userId", user.getId());

        //勤怠画面へ
        return "redirect:/home";
    }
}
