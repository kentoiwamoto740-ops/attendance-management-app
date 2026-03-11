package com.iwamoto.attendance.repository;

import com.iwamoto.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    Attendance findByUserIdAndWorkDate(Long userId, LocalDate workDate);
    List<Attendance> findByUserIdOrderByWorkDateDesc(Long userId);
}
