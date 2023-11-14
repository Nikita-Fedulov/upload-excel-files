package com.example.uploadexcelfiles.repository;

import com.example.uploadexcelfiles.model.RequestCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RequestCountRepository extends JpaRepository<RequestCount, Long> {
    RequestCount findByTimestamp(LocalDateTime timestamp);

}
