package com.iwamoto.attendance.service;

import com.iwamoto.attendance.entity.Attendance;
import com.iwamoto.attendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // 出勤打刻
    public void startWork(Long userId) {

        Attendance existing = attendanceRepository.findByUserIdAndWorkDate(userId, LocalDate.now());

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

        Attendance attendance = attendanceRepository.findByUserIdAndWorkDate(userId, LocalDate.now());

        if (attendance == null) {
            throw new RuntimeException("出勤打刻がありません");
        }

        if (attendance.getEndTime() != null) {
            throw new IllegalStateException("すでに退勤済みです");
        }
        attendance.setEndTime(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }
}