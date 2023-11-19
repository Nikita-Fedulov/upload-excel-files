package com.example.uploadexcelfiles.repository;

import com.example.uploadexcelfiles.model.AddressValue;
import com.example.uploadexcelfiles.model.ExcelAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressValueRepository extends JpaRepository<AddressValue, Long> {

    AddressValue findByObjectId(int objectId);

}
