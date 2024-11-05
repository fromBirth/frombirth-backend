// src/main/java/com/project/erpre/AppConfig.java

package com.choongang.frombirth_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig 클래스는 애플리케이션의 설정과 관련된 Bean(빈)들을 정의하고 관리합니다.
 * 여기서는 ModelMapper와 RestTemplate과 같은 외부 라이브러리를 스프링 컨텍스트에 빈으로 등록합니다.
 */
@Configuration  // 이 어노테이션은 이 클래스가 스프링 설정 클래스임을 나타내며,
// 스프링이 해당 클래스의 메서드들을 호출하여 빈을 생성하고 관리합니다.
public class AppConfig {

    /**
     * ModelMapper 빈 설정 메서드입니다.
     * ModelMapper는 엔티티와 DTO 간의 자동 매핑을 지원하여 객체 변환을 간단하게 합니다.
     *
     * @return 스프링이 관리할 ModelMapper 객체
     */
    @Bean  // 이 어노테이션은 메서드에서 반환된 객체를 스프링 빈으로 등록합니다.
    public ModelMapper modelMapper() {
        return new ModelMapper();  // ModelMapper 객체를 생성하여 반환
    }

    /**
     * RestTemplate 빈 설정 메서드입니다.
     * RestTemplate는 RESTful API 호출을 간편하게 해주는 스프링의 HTTP 클라이언트입니다.
     *
     * @return 스프링이 관리할 RestTemplate 객체
     */
    @Bean  // RestTemplate 객체를 빈으로 등록
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Dotenv 라이브러리를 사용해 .env 파일의 환경 변수를 로드합니다.
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    /**
     * 환경 변수를 가져오는 메서드입니다.
     *
     * @param key 가져올 환경 변수의 이름
     * @return 환경 변수의 값
     */
    public static String getEnv(String key) {
        return dotenv.get(key);
    }

    // 환경 변수를 시스템 속성으로 설정하여 application.properties에서 참조할 수 있도록 합니다.
    static {
        // DB 비밀번호 설정
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        // JWT 비밀 키 설정
        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));

        // 카카오 API 키 설정
        System.setProperty("KAKAO_REST_API_KEY", dotenv.get("KAKAO_REST_API_KEY"));
        System.setProperty("KAKAO_CLIENT_SECRET", dotenv.get("KAKAO_CLIENT_SECRET"));
    }
}
