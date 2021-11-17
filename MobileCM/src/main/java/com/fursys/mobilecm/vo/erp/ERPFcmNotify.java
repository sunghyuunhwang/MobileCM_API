package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPFcmNotify {
	
	private String send_from_system;
    private String send_to_system;
    private String com_scd;
    private String title;
    private String message;
    private String user_id;
    
}


