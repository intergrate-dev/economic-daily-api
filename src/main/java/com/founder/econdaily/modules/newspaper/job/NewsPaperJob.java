package com.founder.econdaily.modules.newspaper.job;

import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.modules.newspaper.service.NewsPaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NewsPaperJob {
	private static Logger logger = LoggerFactory.getLogger(NewsPaperJob.class);

	@Autowired
	private NewsPaperService newsPaperService;

	@Scheduled(cron = "0 0 * * * ?")
	protected void executeInternal() {
		try {
			long time_1 = System.currentTimeMillis();
			newsPaperService.cacheNewtestPapers();
			logger.info("-------------------- job execute time: {}, take times: {} s ----------------",
					DateParseUtil.dateToString(new Date(), DateParseUtil.DATETIME_STRICK), (System.currentTimeMillis() - time_1)/1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
