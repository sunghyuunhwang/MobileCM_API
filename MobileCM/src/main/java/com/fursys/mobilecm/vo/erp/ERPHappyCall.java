package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPHappyCall {
	
	private String biztalkmessage = "";
	private String message = "";
	private String attachmentUrl = "";
	private String fromNm = "";
	private String fromNo = "";
	private String client_ssec = "";
	private long send_seq = 0;
	private String orm_purcst = "";
	private String send_dt = "";
	private String attachmentName = "";
	private String templateCode = "";
	
}


