package com.are.gescob.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Alert;
import com.are.gescob.entity.ManagementType;
import com.are.gescob.entity.User;
import com.are.gescob.model.ManagementTypeRepository;

@Controller
public class ManagementTypeController {

	@Autowired
	ManagementTypeRepository repository;
	
	@GetMapping("/management")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new ManagementType(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/management")
	public ModelAndView add(@Valid ManagementType management, BindingResult result,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		management.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			
			switch(action) {
			case "edit":
				model.addAttribute("management", management);
				return new ModelAndView("management_edit",model);
				
			case "add":
				return getView(management, alert, user.getAccount());
			case "remove":
				model.addAttribute("management", management);
				return new ModelAndView("management_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				
				Optional<ManagementType> temp = repository.findByCodeAndAccount(user.getAccount(), management.getCode());
				if (temp.isPresent()) {
					alert.setLevel(Alert.DANGER);
					alert.setMessage("Code exist in database");
					return getView(management, alert, user.getAccount());
					
				}
				
				management.setCreatedDate(new java.util.Date());
				management.setCreatedUser(user);
			}
			
			ManagementType saved = repository.save(management);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.delete(management);
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new ManagementType(),alert,user.getAccount());
	}
	
	@GetMapping("/management/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		ManagementType management = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("management_edit");
		view.addObject("alert", new Alert());
		view.addObject("management", management);
		
		return view;
		
	}
	
	@GetMapping("/management/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		ManagementType management = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("management_remove");
		view.addObject("alert", new Alert());
		view.addObject("management", management);
		
		return view;
		
	}
	
	public ModelAndView getView (ManagementType management, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("management");
		
		Iterable<ManagementType> managements = repository.findAllOrderByName(account);
		
		view.addObject("managements", managements);
		view.addObject("management", management);
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView dataIntegrityViolationExceptionHandler( Exception ex, Model modelo, HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		Alert alert = new Alert();
		alert.setLevel(Alert.DANGER);
		alert.setMessage("Database error. Violation integrity data when was to remove management record");
		return getView(new ManagementType(),alert,userLogin.getAccount());
	}
}
