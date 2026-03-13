package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.entity.Attendance;
import com.iwamoto.attendance.service.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        long totalMinutes = attendanceList.stream()
                .filter(a -> a.getStartTime() != null && a.getEndTime() != null)
                .filter(a -> a.getWorkDate().getMonthValue() == currentMonth)
                .filter(a -> a.getWorkDate().getYear() == currentYear)
                .mapToLong(a -> Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
                //.mapToLong(a -> java.time.Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
                .sum();

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        String totalWorkTime = hours + "時間" + minutes + "分";

        model.addAttribute("attendance", attendanceList);
        model.addAttribute("totalWorkTime", totalWorkTime);

        return "attendance_history";

    }
}