package com.welldo.boot.web;

import com.welldo.boot.entity.User;
import com.welldo.boot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


// Controller使用@Controller标记,而不是@Component:
@Controller
public class UserController {

	public static final String KEY_USER = "__user__";

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		Map<String, Object> model = new HashMap<>();
		if (user != null) {
			model.put("user", model);
		}
		return new ModelAndView("index.html", model);
	}

	@GetMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register.html");
	}

	@PostMapping("/register")
	public ModelAndView doRegister(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   @RequestParam("name") String name) {
		try {
			User user = userService.register(email, password, name);
			logger.info("user registered: {}", user.getEmail());
		} catch (RuntimeException e) {
			Map<String, String> map = new HashMap<>();
			map.put("email",email);
			map.put("error","Register failed");

			return new ModelAndView("register.html", map);
		}
		return new ModelAndView("redirect:/signin");
	}

	@GetMapping("/signin")
	public ModelAndView signin(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		if (user != null) {
			return new ModelAndView("redirect:/profile");
		}
		return new ModelAndView("signin.html");
	}

	@PostMapping("/signin")
	public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password") String password,
                                 HttpSession session) {

		try {
			User user = userService.signin(email, password);
			session.setAttribute(KEY_USER, user);
		} catch (RuntimeException e) {
			Map<String, String> map = new HashMap<>();
			map.put("email",email);
			map.put("error","Signin failed");

			return new ModelAndView("signin.html", map);
		}
		return new ModelAndView("redirect:/profile");
	}

	@GetMapping("/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}

		Map<String, User> map = new HashMap<>();
		map.put("user",user);

		return new ModelAndView("profile.html", map);
	}

	@GetMapping("/signout")
	public String signout(HttpSession session) {
		session.removeAttribute(KEY_USER);
		return "redirect:/signin";
	}
}
