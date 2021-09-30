package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsOrder extends TmsOrderBase{
	private String updateDate;
	private String closeTime;
	private String adminCode;
	private String postCode;
	private String deliveryVolume;
	private String openTime;
	private String deliveryCount;
	private String seq;
}
