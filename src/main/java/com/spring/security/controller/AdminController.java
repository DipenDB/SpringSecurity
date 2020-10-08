package com.spring.security.controller;


import com.spring.security.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("index")
	public String getAdminIndex() {return "admin/index";}




	
}
