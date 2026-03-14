package com.iwamoto.attendance.service;

import com.iwamoto.attendance.entity.Attendance;
import com.iwamoto.attendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // 出勤打刻
    public void startWork(Long userId) {

        //Attendance existing = attendanceRepository.findByUserIdAndWorkDate(userId, LocalDate.now());
        Attendance existing = attendanceRepository
                .findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElse(null);

        if (existing != null) {
            throw new IllegalStateException("すでに出勤済みです");
        }

        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setWorkDate(LocalDate.now());
        attendance.setStartTime(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }

    // 退勤打刻
    public void endWork(Long userId) {

        //Attendance attendance = attendanceRepository.findByUserIdAndWorkDate(userId, LocalDate.now());
        Attendance attendance = attendanceRepository
                .findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("出勤打刻がありません"));

        if (attendance.getEndTime() != null) {
            throw new IllegalStateException("すでに退勤済みです");
        }
        attendance.setEndTime(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }

    //
    public Attendance getTodayAttendance(Long userId){
        //return attendanceRepository.findByUserIdAndWorkDate(userId, LocalDate.now());
        return attendanceRepository
                .findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElse(null);
    }

    //
    public List<Attendance> getAttendanceHistory(Long userId) {
        return attendanceRepository.findByUserIdOrderByWorkDateDesc(userId);
    }

    public void startBreak(Long userId) {

        Attendance attendance = attendanceRepository
                .findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("出勤していません"));

        if (attendance.getEndTime() != null) {
            throw new IllegalStateException("すでに退勤しています");
        }

        if (attendance.getBreakStartTime() != null) {
            throw new IllegalStateException("すでに休憩開始しています");
        }
        attendance.setBreakStartTime(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }

    public void endBreak(Long userId) {

        Attendance attendance = attendanceRepository
                .findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("出勤していません"));

        if (attendance.getBreakStartTime() == null) {
            throw new IllegalStateException("休憩開始していません");
        }

        if (attendance.getBreakEndTime() != null) {
            throw new IllegalStateException("すでに休憩終了しています");
        }

        attendance.setBreakEndTime(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }
}