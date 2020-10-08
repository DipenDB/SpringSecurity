package com.spring.security.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("management")
public class ManagementController {

	@GetMapping("index")
	public String getManagementIndex() {
		return "management/index";}
}
