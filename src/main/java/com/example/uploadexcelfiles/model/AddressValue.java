package com.example.uploadexcelfiles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address_values")
public class AddressValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_detail_id", referencedColumnName = "id")
    private AddressDetail addressDetail;

    @Column(name = "object_id")
    private int objectId;

    @Column(name = "object_level_id")
    private int objectLevelId;

    @Column(name = "operation_type_id")
    private int operationTypeId;

    @Column(name = "object_guid")
    private String objectGuid;

    @Column(name = "address_type")
    private int addressType;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "region_code")
    private int regionCode;

//    @Column(name = "is_active")
//    private boolean isActive;
//
//    @Column(name = "path")
//    private String path;
//
//    @Column(name = "postal_code")
//    private String postalCode;
//
//    @Column(name = "ifns_ul")
//    private String ifnsUl;
//
//    @Column(name = "ifns_fl")
//    private String ifnsFl;
//
//    @Column(name = "ifns_tul")
//    private String ifnsTul;
//
//    @Column(name = "ifns_tfl")
//    private String ifnsTfl;

//    @Column(name = "okato")
//    private String okato;
//
//    @Column(name = "oktmo")
//    private String oktmo;
//
//    @Column(name = "kladr_code")
//    private String kladrCode;

//    @Column(name = "cadastral_number")
//    private String cadastralNumber;
//
//    @Column(name = "apart_building")
//    private String apartBuilding;
//
//    @Column(name = "remove_cadastr")
//    private String removeCadastr;
//
//    @Column(name = "oktmo_budget")
//    private String oktmoBudget;
//
//    @Column(name = "successor_object_id")
//    private int successorObjectId;
//
//    @Column(name = "successor_object_guid")
//    private String successorObjectGuid;

}