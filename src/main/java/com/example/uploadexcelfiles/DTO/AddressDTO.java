package com.example.uploadexcelfiles.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private int object_id;
    private int object_level_id;
    private int operation_type_id;
    private String object_guid;
    private int address_type;
    private String full_name;
    private int region_code;
    private boolean is_active;
    private String path;
    private AddressDetailsDTO address_details;
    private SuccessorRefDTO successor_ref;
    private List<HierarchyDTO> hierarchy;

}
