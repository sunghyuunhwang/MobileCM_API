package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPVehicleList {
	
	private String vehicleId;
	private String vehicleName;
	private String weight = "10";	//최대 적재량 (단위 : ton)
	private String vehicleType = "01"; // 차량 유형(01:상온, 02: 냉장/냉동, 99:기타) 차량정보와 배송지정보의 차량유형을 기준으로 배차 시 유형이 같은 배송지와 차량으로 배차 됩니다.
	private String zoneCode = "";	//권역코드
	private String skillPer = "100"; //숙련도
	
}


