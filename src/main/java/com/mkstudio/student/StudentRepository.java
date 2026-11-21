package com.mkstudio.student;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, UUID id);

    long countByStatus(StudentStatus status);

    @Query("""
            SELECT COUNT(DISTINCT s.id)
            FROM Student s
            WHERE EXISTS (
                SELECT 1
                FROM StudentAnamnesis a
                WHERE a.student = s
                  AND a.hasHealthAlerts = true
                  AND a.createdAt = (
                      SELECT MAX(a2.createdAt)
                      FROM StudentAnamnesis a2
                      WHERE a2.student = s
                  )
            )
            """)
    long countWithHealthAlerts();

    @Query("""
            SELECT s
            FROM Student s
            WHERE (:status IS NULL OR s.status = :status)
              AND (LOWER(s.fullName) LIKE :searchPattern OR s.phone LIKE :searchPattern)
              AND (
                  :onlyHealthAlerts = false
                  OR EXISTS (
                      SELECT 1
                      FROM StudentAnamnesis a
                      WHERE a.student = s
                        AND a.hasHealthAlerts = true
                        AND a.createdAt = (
                            SELECT MAX(a2.createdAt)
                            FROM StudentAnamnesis a2
                            WHERE a2.student = s
                        )
                  )
              )
            """)
    Page<Student> searchStudents(
            @Param("status") StudentStatus status,
            @Param("searchPattern") String searchPattern,
            @Param("onlyHealthAlerts") boolean onlyHealthAlerts,
            Pageable pageable
    );
}
