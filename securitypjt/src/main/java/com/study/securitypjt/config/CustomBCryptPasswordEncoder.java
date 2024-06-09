package com.study.securitypjt.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// 순환 참조발생으로 인한 분리
// SpringConfig 객체 생성 중 PrincipalOauth2UserService 객체 의존으로 인해 PrincipalOauth2UserService 생성
// PrincipalOauth2UserService 생성 중 빈으로 등록한 BCryptPasswordEncoder 참조로 인한 순환 참조 발생
// SpringConfig -> PrincipalOauth2UserService -> SpringConfig
@Component
public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder {

}
