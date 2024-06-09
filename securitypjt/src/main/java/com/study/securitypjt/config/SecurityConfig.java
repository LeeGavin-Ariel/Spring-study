package com.study.securitypjt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.study.securitypjt.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨(활성화)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured, preAuthorize 어노테이션 활성화 
public class SecurityConfig {
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	// 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
//	@Bean
//	public BCryptPasswordEncoder encodePwd() {
//		return new BCryptPasswordEncoder();
//	}
	
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
						.loginPage("/loginForm") // 구글 로그인이 완료된 후 후처리 필요
						// 1. 코드 받기(인증) 2. 액세스 토큰(권한) 3. 사용자 프로필 정보 가져옴
						// 4-1. 정보를 토대로 회원가입 자동 진행
						// 4-2. 필요 정보 부족 시 추가 정보 입력 진행
						// 구글 로그인 완료시 코드를 받는 것이 아님 -> 액세스 토큰, 사용자 프로필 정보를 받는다.
						.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(principalOauth2UserService))
		);

        return http.build();
    }
}
