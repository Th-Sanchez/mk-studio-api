package com.mkstudio.student;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentAnamnesisRepository extends JpaRepository<StudentAnamnesis, UUID> {

    Optional<StudentAnamnesis> findFirstByStudentIdOrderByCreatedAtDesc(UUID studentId);

    Optional<StudentAnamnesis> findByIdAndStudentId(UUID id, UUID studentId);

    @Query("""
            SELECT a
            FROM StudentAnamnesis a
            WHERE a.student.id IN :studentIds
              AND a.createdAt = (
                  SELECT MAX(a2.createdAt)
                  FROM StudentAnamnesis a2
                  WHERE a2.student.id = a.student.id
              )
            """)
    List<StudentAnamnesis> findLatestByStudentIds(@Param("studentIds") Collection<UUID> studentIds);
}
