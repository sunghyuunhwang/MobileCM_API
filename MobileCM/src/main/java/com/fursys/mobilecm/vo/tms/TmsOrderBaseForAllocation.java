package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsOrderBaseForAllocation extends TmsOrderBase{
	private String expectedArrivalTime;
	private String expectedDepartureTime;
}
