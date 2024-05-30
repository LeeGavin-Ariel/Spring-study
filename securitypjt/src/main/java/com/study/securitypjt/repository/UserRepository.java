package com.study.securitypjt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.securitypjt.domain.User;

// CRUD 함수를 JpaRepository가 들고있음.
// @Repository 선언을 안해도 IoC 됨 -> JpaRepository를 상속했으므로
public interface UserRepository extends JpaRepository<User, Integer> {

}
