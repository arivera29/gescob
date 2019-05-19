package com.are.gescob.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Alert;
import com.are.gescob.entity.User;
import com.are.gescob.model.AccountRepository;
import com.are.gescob.model.UserRepository;
import com.are.gescob.util.EncriptarMD5;


@Controller
public class LoginController {

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	UserRepository userRepository;
	
	
	@Autowired
	MessageSource messageSource;
	
	@PostMapping("/login")
	public ModelAndView loginUser(@RequestParam("user") String username, 
			@RequestParam("password") String password, 
			HttpSession session, Model model) {
		
		
		User user = userRepository.findByUsernameAndPassword(username, EncriptarMD5.getMD5(password));
		
		if (user == null) {
			ModelAndView view = new ModelAndView("index");
			view.addObject("user", username);
			view.addObject("password", password);
			Alert alert = new Alert();
			alert.setLevel(Alert.INFO);
			alert.setMessage("User/Password no valid, verify please");
			view.addObject("alert", alert);
			return view;
		}
		
		session.setAttribute("user", user);
		
		
		return new ModelAndView("redirect:/main");
	}
	
}
