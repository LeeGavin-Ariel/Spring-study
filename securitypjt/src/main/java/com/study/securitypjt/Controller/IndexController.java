package com.study.securitypjt.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.securitypjt.config.auth.PrincipalDetails;
import com.study.securitypjt.domain.User;
import com.study.securitypjt.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, 
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		// principalDetails 가 userDetails 상속받는다.
		// 일반적으로  UserDetails 타입이 Authentication에 들어온다.
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getUser());

		System.out.println("userDetails : " + userDetails.getUser());

		return "세션 정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) { // 타입은 다르지만 접근 가능 방법 2가지
		// OAuth 로그인을 하면 OAuth2User 타입이 Authentication에 들어온다.
		// userDetails, principalDetails 같은 타입
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("userDetails : " + oauth2User.getAttributes());
		System.out.println("oauth2User : "+oauth.getAttributes());
		return "OAuth 세션 정보 확인하기";
	}
	
	@GetMapping({"", "/"})
	public String index() {
		// 머스테치 기본 폴더 : src/main/resources/
		// 뷰리버 설정 : templates(prefix), .mustache(suffix) -> 생략가능
		return "index";
	}
	
	// Authentication 객체에 PrincipalDetails를 저장하기 위해서 Service를 만들었
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : "+principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	// 초기 spring securiry가 해당 주소를 낚아챔 -> SecurityConfig 작성 후 작동 안함
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);

		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@Secured("ROLE_ADMIN") // 해당 권한만 접근 가능 
	@GetMapping("/info")
	public String info() {
		return "info";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	@GetMapping("/data")
	public String data() {
		return "data";
	}
}
