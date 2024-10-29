package com.choongang.frombirth_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class FrombirthBackendApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(FrombirthBackendApplication.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public static void main(String[] args) {
		SpringApplication.run(FrombirthBackendApplication.class, args);
	}


	@Override
	public void run(String... args) {
		try {
			// 데이터베이스에서 현재 시각 가져오기 (PostgreSQL의 경우 CURRENT_TIMESTAMP 사용)
			String currentDbTime = jdbcTemplate.queryForObject("SELECT CURRENT_TIMESTAMP", String.class);

			logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
			logger.info("데이터베이스 현재 시간: {}", currentDbTime);
			logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

		} catch (Exception e) {
			logger.error("데이터베이스에서 현재 시간을 가져오는 데 실패했습니다.", e);
		}
	}
}
