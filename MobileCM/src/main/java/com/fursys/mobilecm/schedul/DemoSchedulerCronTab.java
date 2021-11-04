package com.fursys.mobilecm.schedul;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fursys.mobilecm.service.ApiErpSigongAsService;
import com.fursys.mobilecm.vo.BaseResponse;

@Component
public class DemoSchedulerCronTab {

	@Autowired ApiErpSigongAsService apiErpSigongAsService;
	@Autowired Environment environment; 
	
	//각 자리는 초, 분, 시, 일, 월, 요일 입니다.
	//"0 0 * * * *" = the top of every hour of every day.
	//"* * * * * *" = 매초 실행 합니다.
	//"*/10 * * * * *" = 매 10초마다 실행한다.
	//"0 0 8-10 * * *" = 매일 8, 9, 10시에 실행한다
	//"0 0 6,19 * * *" = 매일 오전 6시, 오후 7시에 실행한다.
	//"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
	//"0 0 9-17 * * MON-FRI" = 오전 9시부터 오후 5시까지 주중(월~금)에 실행한다.
	//"0 0 0 25 12 ?" = every Christmas Day at midnight
	
	//@Scheduled(cron = "*/10 * * * * *")
	
	@Scheduled(cron = "0 0 7 * * MON-SAT")	// 월~토 오전 7시	
	public void cronJobSch() {

/*		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println("Java cron job expression:: " + strDate);

		//String isScheduledRun
		System.out.println("Scheduled is run? ===>" + environment.getProperty("scheduled.run"));
		
		System.out.println("Scheduled work1 ===>" + environment.getProperty("scheduled.work1"));
*/
		
		//if 
/*		
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();				
//			response = apiErpSigongAsService.erp_selectScheduledtFcmNotifyList(params);			
			if (!"200".equals(response.getResultCode())) {	        		
        		return ;
        	}
			        				
		} catch (Exception e) {
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return;
		}
		
		response.setResultCode("200");
*/		
		return;
		
				
   }
	
}
