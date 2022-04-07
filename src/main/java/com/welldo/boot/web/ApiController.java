package com.welldo.boot.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.welldo.boot.entity.User;
import com.welldo.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	UserService userService;

	@GetMapping("/users")
	public String users() {
		return "xxxxxxxxxxx";
	}

	//@Operation可以对API进行描述，@Parameter可以对参数进行描述
	@Operation(summary = "Get specific user object by it's id.")
	@GetMapping("/users/{id}")
	public User user(@Parameter(description = "id of the user.") @PathVariable("id") long id) {
		return userService.getUserById(id);
	}

	@PostMapping("/signin")
	public Map<String, String> signin(@RequestBody SignInRequest signinRequest) {
		try {
			User user = userService.signin(signinRequest.email, signinRequest.password);
			Map<String, String> map = new HashMap<>();
			map.put("user",user.toString());

			return map;
		} catch (Exception e) {
			Map<String, String> map = new HashMap<>();
			map.put("error","SIGNIN_FAILED");
			map.put("message",e.getMessage());
			return map;
		}
	}

	public static class SignInRequest {
		public String email;
		public String password;
	}
}
