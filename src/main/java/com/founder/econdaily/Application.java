package com.founder.econdaily;

import cn.licoy.encryptbody.annotation.EnableEncryptBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEncryptBody
@EnableScheduling
@SpringBootApplication
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
		logger.info("发送服务启动 ");
	}
}