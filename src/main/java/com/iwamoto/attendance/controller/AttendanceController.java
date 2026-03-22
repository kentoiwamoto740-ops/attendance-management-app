package com.iwamoto.attendance.controller;

import com.iwamoto.attendance.entity.Attendance;
import com.iwamoto.attendance.service.AttendanceService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    //トップ画面
    @GetMapping("/attendance")
    public String attendance(Model model, HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        var attendance = attendanceService.getTodayAttendance(userId);

        model.addAttribute("attendance", attendance);

        return "attendance";
    }

    //出勤
    @PostMapping("/start")
    public String startWork(HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        attendanceService.startWork(userId);

        return "redirect:/attendance";
    }

    //退勤
    @PostMapping("/end")
    public String endWork(HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");

        attendanceService.endWork(userId);

        return "redirect:/attendance";
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

//        long totalMinutes = attendanceList.stream()
//                .filter(a -> a.getStartTime() != null && a.getEndTime() != null)
//                .filter(a -> a.getWorkDate().getMonthValue() == currentMonth)
//                .filter(a -> a.getWorkDate().getYear() == currentYear)
//                .mapToLong(a -> Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
//                //.mapToLong(a -> java.time.Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
//                .sum();
        long totalMinutes = attendanceList.stream()
                .filter(a -> a.getStartTime() != null && a.getEndTime() != null)
                .filter(a -> a.getWorkDate().getMonthValue() == currentMonth)
                .filter(a -> a.getWorkDate().getYear() == currentYear)
                .mapToLong(a -> {

                    Duration work = Duration.between(a.getStartTime(), a.getEndTime());

                    if (a.getBreakStartTime() != null && a.getBreakEndTime() != null) {
                        Duration breakTime = Duration.between(a.getBreakStartTime(), a.getBreakEndTime());
                        work = work.minus(breakTime);
                    }

                    return work.toMinutes();

                })
                .sum();

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        String totalWorkTime = hours + "時間" + minutes + "分";

        model.addAttribute("attendance", attendanceList);
        model.addAttribute("totalWorkTime", totalWorkTime);

        return "attendance_history";

    }

    //
    @PostMapping("/break/start")
    public String startBreak(HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        attendanceService.startBreak(userId);

        return "redirect:/attendance";
    }

    @PostMapping("/break/end")
    public String endBreak(HttpSession session) {

        //Long userId = 1L;
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        attendanceService.endBreak(userId);

        return "redirect:/attendance";
    }

    //CSVダウンロード
    @GetMapping("/attendance/csv")
    public void downloadCsv(HttpServletResponse response, HttpSession session) throws IOException {

        Long userId = (Long) session.getAttribute("userId");

        // CSV設定
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=attendance.csv");

        // データ取得
        List<Attendance> attendanceList = attendanceService.getAttendanceHistory(userId);

        // 書き込み準備
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("\uFEFF");

        // ヘッダー
        writer.println("日付,出勤時間,退勤時間,勤務時間");

        // フォーマット
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // データ書き込み
        for (Attendance a : attendanceList) {

            String workDate = a.getWorkDate().toString();

            String startTime = (a.getStartTime() != null)
                    ? a.getStartTime().format(timeFormatter)
                    : "";

            String endTime = (a.getEndTime() != null)
                    ? a.getEndTime().format(timeFormatter)
                    : "";

            String workingHours = (a.getWorkingHours() != null)
                    ? a.getWorkingHours()
                    : "";
            writer.println(workDate + "," + startTime + "," + endTime + "," + workingHours);
        }

        writer.flush();
    }
}