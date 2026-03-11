package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.entity.Attendance;
import com.iwamoto.attendance.service.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    //トップ画面
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        var attendance = attendanceService.getTodayAttendance(userId);

        model.addAttribute("attendance", attendance);

        return "home";
    }

    //出勤
    @PostMapping("/start")
    public String startWork(HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        attendanceService.startWork(userId);

        return "redirect:/home";
    }

    //退勤
    @PostMapping("/end")
    public String endWork(HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        attendanceService.endWork(userId);

        return "redirect:/home";
    }

    //履歴
    @GetMapping("/history")
    public String history(Model model, HttpSession session) {
        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        List<Attendance> attendanceList = attendanceService.getAttendanceHistory(userId);
        model.addAttribute("attendance", attendanceList);

        return "attendance_history";

    }
}