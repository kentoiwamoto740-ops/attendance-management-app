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

    @PostMapping("/start")
    public String startWork() {

        Long userId = 1L;

        attendanceService.startWork(userId);

        return "redirect:/";
    }
}