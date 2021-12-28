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
public class JobSchedulerCronTab {

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
	
	
	@Scheduled(cron = "0 0 7 * * MON-SAT")	// 월~토 오전 7시
//	@Scheduled(cron = "*/10 * * * * *")	// 월~토 오전 7시	
	public void cronJobSch() {
			
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params = new HashMap<String, Object>();
		String job = "", seq_no = "", count = "";

		try {
			String isRun = environment.getProperty("scheduled.run");
	
			if (!"yes".equals(isRun)) return;
	
			job = environment.getProperty("scheduled.job1");
	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date now = new Date();
			String strDate = sdf.format(now);			
			
			if ("erp_selectScheduledtFcmNotifyList".equals(job)) {			
				try {
					
					System.out.println(String.format("Scheduled %s is run on %s", job, strDate));
					
					params = new HashMap<String, Object>();									
					params.put("schedule_server", "MOBILECM");
					params.put("schedule_id", job);
					params.put("schedule_name", "상차전반품PUSH");
					
					response = apiErpSigongAsService.erp_startScheduleResult(params);					
					seq_no = response.getResultCount();

					params = new HashMap<String, Object>();				
					response = apiErpSigongAsService.erp_selectScheduledtFcmNotifyList(params);			
					if (!"200".equals(response.getResultCode())) {
						params = new HashMap<String, Object>();									
						params.put("schedule_server", "MOBILECM");
						params.put("schedule_id", job);
						params.put("schedule_name", "상차전반품PUSH");
						params.put("result", "실패");
						params.put("remark", String.format("resultcode=%s, resultMessage=%s", response.getResultCode(), response.getResultMessage()));					
									
						response = apiErpSigongAsService.erp_startScheduleResult(params);
						
		        		return ;
		        	}
					
					count = response.getResultCount();
					params = new HashMap<String, Object>();
					params.put("seq_no", seq_no);
					params.put("result", "성공");
					params.put("remark", String.format("Send[%s]", count));					
					response = apiErpSigongAsService.erp_finishScheduleResult(params);
					
				} catch (Exception e) {
					System.out.println(e.toString());			
					response.setResultCode("5001");
					response.setResultMessage(e.toString());
					
					if ("".equals(seq_no)) {
						params = new HashMap<String, Object>();									
						params.put("schedule_server", "MOBILECM");
						params.put("schedule_id", job);
						params.put("schedule_name", "상차전반품PUSH");
						params.put("result", "실패");
						params.put("remark", e.toString());
						response = apiErpSigongAsService.erp_startScheduleResult(params);
					} else {
						params = new HashMap<String, Object>();
						params.put("seq_no", seq_no);
						params.put("result", "실패");
						params.put("remark", e.toString());
						response = apiErpSigongAsService.erp_finishScheduleResult(params);
					}
					
					return;
				}			
				response.setResultCode("200");			
			}

		} catch(Exception e) {
			System.out.println(e.toString());
			
			params = new HashMap<String, Object>();									
			params.put("schedule_server", "MOBILECM");
			params.put("schedule_id", job);
			params.put("schedule_name", "상차전반품PUSH");
			params.put("result", "실패");
			params.put("remark", e.toString());					
						
			response = apiErpSigongAsService.erp_startScheduleResult(params);
			
		}
		
		return;					
	}
	
	//@Scheduled(cron = "0 59 23 * * *")	// 매일 23:59분	
	@Scheduled(cron = "0 59 8 * * *")	// 매일 23:59분
	public void cronJobSch2() {

		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params = new HashMap<String, Object>();
		String job = "", seq_no = "", count = "";

		try {
			String isRun = environment.getProperty("scheduled.run");
	
			if (!"yes".equals(isRun)) return;
	
			job = environment.getProperty("scheduled.job2");
	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date now = new Date();
			String strDate = sdf.format(now);			
			
			if ("erp_sigongDelivery".equals(job)) {
				try {
					
					System.out.println(String.format("Scheduled %s is run on %s", job, strDate));
					
					params = new HashMap<String, Object>();									
					params.put("schedule_server", "MOBILECM");
					params.put("schedule_id", job);
					params.put("schedule_name", "시공납기");
					
					response = apiErpSigongAsService.erp_startScheduleResult(params);					
					seq_no = response.getResultCount();

					params = new HashMap<String, Object>();				
					response = apiErpSigongAsService.erp_sigongDelivery(params);			
					if (!"200".equals(response.getResultCode())) {
						params = new HashMap<String, Object>();									
						params.put("schedule_server", "MOBILECM");
						params.put("schedule_id", job);
						params.put("schedule_name", "시공납기");
						params.put("result", "실패");
						params.put("remark", String.format("resultcode=%s, resultMessage=%s", response.getResultCode(), response.getResultMessage()));					
									
						response = apiErpSigongAsService.erp_startScheduleResult(params);
						
		        		return ;
		        	}
					
					count = response.getResultCount();
					params = new HashMap<String, Object>();
					params.put("seq_no", seq_no);
					params.put("result", "성공");
					params.put("remark", String.format("Send[%s]", count));					
					response = apiErpSigongAsService.erp_finishScheduleResult(params);
					
				} catch (Exception e) {
					System.out.println(e.toString());			
					response.setResultCode("5001");
					response.setResultMessage(e.toString());
					
					if ("".equals(seq_no)) {
						params = new HashMap<String, Object>();									
						params.put("schedule_server", "MOBILECM");
						params.put("schedule_id", job);
						params.put("schedule_name", "시공납기");
						params.put("result", "실패");
						params.put("remark", e.toString());
						response = apiErpSigongAsService.erp_startScheduleResult(params);
					} else {
						params = new HashMap<String, Object>();
						params.put("seq_no", seq_no);
						params.put("result", "실패");
						params.put("remark", e.toString());
						response = apiErpSigongAsService.erp_finishScheduleResult(params);
					}
					
					return;
				}			
				response.setResultCode("200");			
			}

		} catch(Exception e) {
			System.out.println(e.toString());
			
			params = new HashMap<String, Object>();									
			params.put("schedule_server", "MOBILECM");
			params.put("schedule_id", job);
			params.put("schedule_name", "시공납기");
			params.put("result", "실패");
			params.put("remark", e.toString());					
						
			response = apiErpSigongAsService.erp_startScheduleResult(params);
			
		}
				
		return;					
	}
	
	//@Scheduled(cron = "*/10 * * * * *")	
	public void cronJobSch99() {
				
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params = new HashMap<String, Object>();
		String job = "", seq_no = "", count = "";

		try {
			String isRun = environment.getProperty("scheduled.run");
	
			System.out.println(String.format("Scheduled %s is run on %s", isRun, isRun));
			
			if (!"yes".equals(isRun)) return;
	
			job = environment.getProperty("scheduled.test");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date now = new Date();
			String strDate = sdf.format(now);
	
			if ("erp_testJob".equals(job)) {
				
				try {					
					System.out.println(String.format("Scheduled %s is run on %s", job, strDate));				
					
					params = new HashMap<String, Object>();
					params.put("schedule_server", "WEBSERVER");
					params.put("schedule_id", job);
					params.put("schedule_name", "테스트JOB");
					
					response = apiErpSigongAsService.erp_startScheduleResult(params);					
					seq_no = response.getResultCount();

					/*
					 * 
					 *  작업
					 * 
					 * 
					 */
					
					params.put("seq_no", seq_no);
					params.put("result", "성공");
					params.put("remark", String.format("Send[%s]", seq_no));
					
					response = apiErpSigongAsService.erp_finishScheduleResult(params);					

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
			}
		} catch(Exception e) {
			System.out.println(e.toString());
			
		}
		
		return;						
	}
	
}
