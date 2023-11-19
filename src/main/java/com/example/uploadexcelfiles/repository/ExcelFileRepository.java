package com.example.uploadexcelfiles.repository;

import com.example.uploadexcelfiles.model.ExcelAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcelFileRepository extends JpaRepository<ExcelAddress, Long> {
    List<ExcelAddress> findByAddressIn(List<String> addresses);
}

