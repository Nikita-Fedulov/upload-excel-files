package com.example.uploadexcelfiles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address_details")
public class AddressDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "addressDetail")
    private AddressValue addressValue;
    private String postal_code;
    private String ifns_ul;
    private String ifns_fl;
//        private String ifns_tul;
//        private String ifns_tfl;
//        private String okato;
//        private String oktmo;
//        private String kladr_code;
//        private String cadastral_number;
//        private String apart_building;
//        private String remove_cadastr;
//        private String oktmo_budget;

}
