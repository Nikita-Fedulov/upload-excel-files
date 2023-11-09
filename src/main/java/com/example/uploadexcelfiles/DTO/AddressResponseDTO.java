package com.example.uploadexcelfiles.DTO;

import com.example.uploadexcelfiles.model.ExcelAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {
    private List<AddressDTO> addresses;

}
