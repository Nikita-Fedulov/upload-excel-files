package com.example.uploadexcelfiles.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetailsDTO {
    private String postal_code;
    private String ifns_ul;
    private String ifns_fl;
    private String ifns_tul;
    private String ifns_tfl;
    private String okato;
    private String oktmo;
    private String kladr_code;
    private String cadastral_number;
    private String apart_building;
    private String remove_cadastr;
    private String oktmo_budget;

}
