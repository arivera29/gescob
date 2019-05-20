package com.are.gescob.controller;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.User;


@Controller
public class HomeController {
	
	
	@Autowired
	MessageSource messageSource;

	@RequestMapping("/")
	public String home(HttpSession session) {
		/*
		 * if (session.getAttribute("account") != null) { return "redirect:/main"; }
		 */
		
		return "redirect:/main";
	}
	
	/*
	 * @RequestMapping("/survey") public String survey() {
	 * 
	 * return "survey"; }
	 */
	
	@RequestMapping("/login")
	public String login(HttpSession session) {
		session.setAttribute("user", null);
		
		return "index";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("user", null);
		
		return "index";
	}
	
	@RequestMapping("/main")
	public ModelAndView main(HttpSession session) {
		
		//User userLogin = (User)session.getAttribute("user");
		
		ModelAndView view =  new ModelAndView("main");
		
	
				
		return view;
	}
	
	@RequestMapping("/options")
	public String options(HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return "redirect:/access_denied";
		}
		
		return "options";
	}
	
	@RequestMapping("/collections_options")
	public String collections_options(HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		if (!"ADM,OPER".contains(userLogin.getRole())) {
			return "redirect:/access_denied";
		}
		
		return "collections_options";
	}
	
	@RequestMapping("/reports")
	public String reports(HttpSession session, Model model) {
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return "redirect:/access_denied";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		model.addAttribute("date", sdf.format(new java.util.Date()));
		
		return "reports";
	}
	
	@RequestMapping("/manager")
	public String manager_home(HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("MNG")) {
			return "redirect:/access_denied";
		}
		
		return "manager_options";
	}
	
	@RequestMapping("/access_denied")
	public String access_denied() {
		
		return "access_denied";
	}
}
