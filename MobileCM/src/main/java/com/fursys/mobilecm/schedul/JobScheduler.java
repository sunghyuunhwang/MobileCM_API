package com.fursys.mobilecm.schedul;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {

	private static Logger logger = LoggerFactory.getLogger(JobScheduler.class);
	
/*	
	@Scheduled(fixedDelay = 5000)
	public void alert() {
		logger.info("현재 시간 : {}", new Date());
	}
*/	

	
}
