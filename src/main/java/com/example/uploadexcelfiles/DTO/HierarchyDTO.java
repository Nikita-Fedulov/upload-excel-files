package com.example.uploadexcelfiles.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HierarchyDTO {
    private int object_id;
    private int object_level_id;
    private String object_guid;
    private String full_name;
    private String full_name_short;
    private String object_type;
}
