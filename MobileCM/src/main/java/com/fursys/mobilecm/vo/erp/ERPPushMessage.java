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
public class ERPPushMessage {
	
	private String token;
	private String command;
	private String message;
	
	private String sti_cd;
}
