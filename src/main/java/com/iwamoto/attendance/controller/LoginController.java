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

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
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

        try{
            User user = userService.login(username, password);
            //セッション保存
            session.setAttribute("userId", user.getId());
            //勤怠画面へ
            return "redirect:/dashboard";
        } catch (IllegalStateException e) {
            //ログイン失敗
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    //ログアウト
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();

        return "redirect:/login";
    }
}
