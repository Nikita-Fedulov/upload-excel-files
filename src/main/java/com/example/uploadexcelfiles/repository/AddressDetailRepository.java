package com.example.uploadexcelfiles.repository;

import com.example.uploadexcelfiles.model.AddressDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDetailRepository extends JpaRepository<AddressDetail, Long> {
}
