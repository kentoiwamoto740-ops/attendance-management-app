package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
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