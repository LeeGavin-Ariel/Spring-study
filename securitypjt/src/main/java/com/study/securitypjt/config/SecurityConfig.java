package com.study.securitypjt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨(활성화)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured, preAuthorize 어노테이션 활성화 
public class SecurityConfig {
	
	// 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable);
        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // DB저장은 ROLE_* 여야 한다.
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
        ).formLogin((formLogin) ->
        		formLogin
        				.loginPage("/loginForm")
        				.loginProcessingUrl("/login") // /login 주소가 호출되면 security가 낚아채서 로그인을 진행해준다.
        				.defaultSuccessUrl("/") // 로그인이 되면 메인페이지로 가도록
        				// .usernameParameter("userId") // loginForm에서 username이 아닌 다른 이름으로 받고 싶은 경우
		).oauth2Login((oauth2) -> 
				oauth2
					.loginPage("/loginForm") // 구글 로그인이 완료된 후 후처리 필
		);

        return http.build();
    }
}
