package com.fursys.mobilecm.vo.tms.reponse;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.tms.TmsVehicle;
import com.fursys.mobilecm.vo.tms.TmsZone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsZoneListResponse  extends BaseResponse{
	private ArrayList<TmsZone> resultData;
}
