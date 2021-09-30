package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPGoGoVanWayPoint {
    private String address1 = "";
    private String address2 = "";
    private String name = "";
    private String mobile_no = "";
    private String request_at = "";
    private String description = "";
    private String company = "";
    private String team = "";    
}
