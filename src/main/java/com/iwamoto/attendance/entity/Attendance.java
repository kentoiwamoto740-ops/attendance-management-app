package com.iwamoto.attendance.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate workDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime breakStartTime;

    private LocalDateTime breakEndTime;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getBreakStartTime() { return breakStartTime; }

    public LocalDateTime getBreakEndTime() { return breakEndTime; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getWorkingHours(){

        if(startTime == null || endTime == null){
            return "";
        }

        Duration duration = Duration.between(startTime, endTime);

        // 休憩時間を引く
        if(breakStartTime != null && breakEndTime != null){
            Duration breakDuration = Duration.between(breakStartTime, breakEndTime);
            duration = duration.minus(breakDuration);
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return hours + "時間" + minutes +"分";
    }

    public void setBreakStartTime (LocalDateTime breakStartTime){ this.breakStartTime = breakStartTime; }

    public void setBreakEndTime (LocalDateTime breakEndTime){ this.breakEndTime = breakEndTime; }

}
