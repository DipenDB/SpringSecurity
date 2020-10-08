package com.spring.security.controller;



import com.spring.security.db.User;
import com.spring.security.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/public")

@CrossOrigin  //For JWT
public class RestApiController {
	@Autowired
	private UserRepository userRepository;
	

	@GetMapping("test1")
	public String test1() {
		return "API Test 1";}

	@GetMapping("test2")
	public String test2() {
		return "API Test 2";}
	
	@GetMapping("test3")
	public List<User> allUser() {
		return this.userRepository.findAll();}



}
