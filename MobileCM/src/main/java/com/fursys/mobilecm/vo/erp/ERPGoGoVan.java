package com.fursys.mobilecm.vo.erp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPGoGoVan {
    private String user_code = "";
    private String customer_order_id = "";
    private String vehicle = "";
    private String pay = "";
    private String appointment_at = "";
    private String remark = "";
    private String amount = "0";
    private ArrayList<ERPGoGoVanWayPoint> waypoint;
    
}
