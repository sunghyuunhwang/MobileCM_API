package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPGubbunInfo {
	private String car_no ; //차랑변호
	private String com_tonsec ; //톤수정보
	private String com_carlansec ; //도착예정시간
	private String car_dnm ; //기사명
	private String car_hpno ; //기사 HP번호
}


