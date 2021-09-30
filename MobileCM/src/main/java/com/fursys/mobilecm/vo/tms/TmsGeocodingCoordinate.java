package com.fursys.mobilecm.vo.tms;

import com.fursys.mobilecm.vo.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsGeocodingCoordinate{
	
	private String matchFlag; //매칭 구분 코드입니다.
	private String lat; //위도 좌표입니다.
	private String lon; //경도 좌표입니다.
	private String latEntr; //주소 입구점 위도 좌표입니다.
	private String lonEntr; //주소 입구점 경도 좌표입니다.
	private String city_do; //서울시, 부산광역시, 대구광역시	출력 좌표에 해당하는 시/도 명칭입니다.
	private String gu_gun; //강서구, 부평구, 도봉구	출력 좌표에 해당하는 군/구 명칭입니다.
	private String eup_myun; //옥천읍, 산청읍, 물금읍	출력 좌표에 해당하는 주소 읍면동 명칭입니다.
	private String legalDong; //방화동, 화곡동, 쌍문동	출력 좌표에 해당하는 법정동 명칭입니다.
	private String legalDongCode; //1132010500 -> '쌍문동'	법정동 명칭에 해당하는 코드 10자리입니다.
	private String adminDong; //방화1동, 방화2동, 화곡1동, 화곡2동	출력 좌표에 해당하는 행정동 명칭입니다.
	private String adminDongCode; //1150056000 -> ‘화곡3동’	행정동 명칭에 해당되는 코드 10자리 입니다.
	private String ri; //금구리, 지리, 대포리	출력 좌표에 해당하는 리 명칭입니다.
	private String bunji; //1-1, 2-1	출력 좌표에 해당하는 지번입니다.
	private String buildingName; //구주소 매칭을 한 경우 건물 명을 반환합니다.
	private String buildingDong; //구주소 매칭을 한 경우 건물 동을 반환합니다.
	private String newMatchFlag; //새(도로명) 주소 좌표 매칭 구분입니다.
	private String newLat; //새주소 매칭을 한 경우, 새주소 위도 좌표를 반환합니다.
	private String newLon; //새주소 매칭을 한 경우, 새주소 경도 좌표를 반환합니다.
	private String newLatEntr; //새주소 매칭을 한 경우, 새주소 위도 입구점 좌표를 반환합니다.
	private String newLonEntr; //새주소 매칭을 한 경우, 새주소 경도 입구점 좌표를 반환합니다.
	private String newRoadName; //새(도로명) 주소 매칭을 한 경우, 길 이름을 반환합니다.
	private String newBuildingIndex; //새(도로명) 주소 매칭을 한 경우, 건물 번호를 반환합니다.
	private String newBuildingName; //새(도로명) 주소 건물명 매칭을 한 경우, 건물 이름을 반환합니다.
	private String newBuildingDong; //새주소 건물을 매칭한 경우 새주소 건물 동을 반환 합니다
	private String newBuildingCateName; //
	private String zipcode; //우편번호
	private String remainder; //정매치 시
	
}
