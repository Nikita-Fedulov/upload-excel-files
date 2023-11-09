package com.example.uploadexcelfiles.repository;

import com.example.uploadexcelfiles.model.ExcelAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelFileRepository extends JpaRepository<ExcelAddress, Long> {
    ExcelAddress findByAddress(String address);
}

