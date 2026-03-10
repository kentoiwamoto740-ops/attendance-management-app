package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    //トップ画面
    @GetMapping("/")
    public String home(Model model) {

        Long userId = 1L;

        var attendance = attendanceService.getTodayAttendance(userId);

        model.addAttribute("attendance", attendance);

        return "home";
    }

    //出勤
    @PostMapping("/start")
    public String startWork() {

        Long userId = 1L;

        attendanceService.startWork(userId);

        return "redirect:/";
    }

    //退勤
    @PostMapping("/end")
    public String endWork() {

        Long userId = 1L;

        attendanceService.endWork(userId);

        return "redirect:/";
    }
}